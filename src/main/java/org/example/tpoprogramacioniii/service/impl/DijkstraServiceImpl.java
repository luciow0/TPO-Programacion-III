package org.example.tpoprogramacioniii.service.impl;

import org.example.tpoprogramacioniii.Enum.AlgorithmEnum;
import org.example.tpoprogramacioniii.Enum.OptimizationCriteriaEnum;
import org.example.tpoprogramacioniii.dto.request.RouteRequestDTO;
import org.example.tpoprogramacioniii.dto.response.LocationResponseDTO;
import org.example.tpoprogramacioniii.dto.response.RouteResponseDTO;
import org.example.tpoprogramacioniii.dto.response.SegmentResponseDTO;
import org.example.tpoprogramacioniii.model.Location;
import org.example.tpoprogramacioniii.model.Segment;
import org.example.tpoprogramacioniii.repository.LocationRepository;
import org.example.tpoprogramacioniii.repository.SegmentRepository;
import org.example.tpoprogramacioniii.service.DijkstraServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Implementación de Dijkstra que construye el grafo desde Neo4j (SegmentRepository).
 * Optimiza por DISTANCE_KM / TIME_MIN / COST_FUEL según OptimizationCriteriaEnum.
 */
@Service
public class DijkstraServiceImpl implements DijkstraServiceI {

    private final SegmentRepository segmentRepository;
    private final LocationRepository locationRepository;

    // Métricas última corrida
    private long lastExecutionTime = 0L;
    private int lastNodesVisited = 0;
    private boolean lastRouteFound = false;
    private double lastTotalDistance = 0.0;
    private double lastTotalTime = 0.0;
    private double lastTotalCost = 0.0;

    @Autowired
    public DijkstraServiceImpl(SegmentRepository segmentRepository,
                               LocationRepository locationRepository) {
        this.segmentRepository = segmentRepository;
        this.locationRepository = locationRepository;
    }

    /* ==================== API requerida por la interfaz ==================== */

    @Override
    public RouteResponseDTO calculateShortestDistanceRoute(String originLocationId, String destinationLocationId) {
        RouteRequestDTO req = new RouteRequestDTO();
        req.setOriginLocationId(originLocationId);
        req.setDestinationLocationId(destinationLocationId);
        req.setOptimizationCriteria(OptimizationCriteriaEnum.DISTANCE_KM);
        return calculateOptimalRoute(req);
    }

    @Override
    public RouteResponseDTO calculateFastestTimeRoute(String originLocationId, String destinationLocationId) {
        RouteRequestDTO req = new RouteRequestDTO();
        req.setOriginLocationId(originLocationId);
        req.setDestinationLocationId(destinationLocationId);
        req.setOptimizationCriteria(OptimizationCriteriaEnum.TIME_MIN);
        return calculateOptimalRoute(req);
    }

    @Override
    public RouteResponseDTO calculateCheapestCostRoute(String originLocationId, String destinationLocationId) {
        RouteRequestDTO req = new RouteRequestDTO();
        req.setOriginLocationId(originLocationId);
        req.setDestinationLocationId(destinationLocationId);
        req.setOptimizationCriteria(OptimizationCriteriaEnum.COST_FUEL);
        return calculateOptimalRoute(req);
    }

    @Override
    public RouteResponseDTO calculateOptimalRoute(RouteRequestDTO request) {
        long start = System.currentTimeMillis();

        if (request == null || request.getOriginLocationId() == null || request.getDestinationLocationId() == null) {
            return error("Parámetros inválidos: origin/destination nulos.");
        }

        OptimizationCriteriaEnum criteria =
                request.getOptimizationCriteria() == null ? OptimizationCriteriaEnum.DISTANCE_KM
                        : request.getOptimizationCriteria();

        // Grafo desde DB
        Map<String, Map<String, Edge>> graph = buildGraphFromDb();

        String origin = request.getOriginLocationId();
        String dest   = request.getDestinationLocationId();

        // Dijkstra
        Map<String, Double> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));
        Set<String> visited = new HashSet<>();

        // incluir nodos que no aparezcan como 'from' pero sí como 'to'
        for (String u : allNodeIds(graph)) dist.put(u, Double.POSITIVE_INFINITY);
        if (!dist.containsKey(origin)) dist.put(origin, 0.0); else dist.put(origin, 0.0);
        pq.add(origin);

        while (!pq.isEmpty()) {
            String u = pq.poll();
            if (!visited.add(u)) continue;
            if (u.equals(dest)) break;

            Map<String, Edge> adj = graph.getOrDefault(u, Collections.emptyMap());
            for (Map.Entry<String, Edge> entry : adj.entrySet()) {
                String v = entry.getKey();
                Edge e = entry.getValue();
                double alt = dist.get(u) + e.weight(criteria);
                if (alt < dist.getOrDefault(v, Double.POSITIVE_INFINITY)) {
                    dist.put(v, alt);
                    prev.put(v, u);
                    pq.remove(v);
                    pq.add(v);
                }
            }
        }

        List<String> nodePath = reconstruct(prev, origin, dest);
        if (nodePath.isEmpty() && !Objects.equals(origin, dest)) {
            // no hay camino
            lastExecutionTime = System.currentTimeMillis() - start;
            lastNodesVisited = visited.size();
            lastRouteFound = false;
            lastTotalDistance = lastTotalTime = lastTotalCost = 0.0;
            return error("No se encontró una ruta válida entre las ubicaciones.");
        }

        // Armar segmentos y métricas
        List<SegmentResponseDTO> segDtos = new ArrayList<>();
        double totalDist = 0, totalTime = 0, totalCost = 0;

        for (int i = 0; i + 1 < nodePath.size(); i++) {
            String a = nodePath.get(i), b = nodePath.get(i + 1);
            Edge e = graph.getOrDefault(a, Collections.emptyMap()).get(b);
            if (e != null) {
                totalDist += e.distance;
                totalTime += e.time;
                totalCost += e.cost;
                // Ajustá el constructor/props a tu DTO real si difiere
                segDtos.add(new SegmentResponseDTO(a, b, e.distance, e.time, e.cost, "HIGHWAY", 100.0));
            }
        }

        // Locations para el path
        Map<String, Location> locMap = loadLocationsMap();
        List<LocationResponseDTO> locDtos = new ArrayList<>();
        for (String id : nodePath) {
            Location l = locMap.get(id);
            String name = (l != null && l.getName() != null) ? l.getName() : id;
            // Ajustá el constructor/props a tu DTO real si difiere
            locDtos.add(new LocationResponseDTO(id, name, 0.0, 0.0, null));
        }

        lastExecutionTime = System.currentTimeMillis() - start;
        lastNodesVisited = visited.size();
        lastRouteFound = true;
        lastTotalDistance = totalDist;
        lastTotalTime = totalTime;
        lastTotalCost = totalCost;

        return new RouteResponseDTO(
                locDtos,
                segDtos,
                totalDist,
                totalTime,
                totalCost,
                AlgorithmEnum.DIJKSTRA,
                lastExecutionTime,
                true,
                "Ruta calculada con Dijkstra (" + criteria.name() + ")"
        );
    }

    @Override
    public boolean hasValidRoute(String originLocationId, String destinationLocationId) {
        RouteResponseDTO r = calculateShortestDistanceRoute(originLocationId, destinationLocationId);
        return r != null && r.isValid();
    }

    @Override
    public Map<String, Object> getAlgorithmPerformanceInfo(RouteRequestDTO request) {
        Map<String, Object> info = new HashMap<>();
        info.put("algorithm", "DIJKSTRA");
        info.put("executionTimeMs", lastExecutionTime);
        info.put("nodesVisited", lastNodesVisited);
        info.put("routeFound", lastRouteFound);
        info.put("totalDistance", lastTotalDistance);
        info.put("totalTime", lastTotalTime);
        info.put("totalCost", lastTotalCost);
        info.put("criteria",
                request != null && request.getOptimizationCriteria() != null
                        ? request.getOptimizationCriteria().name()
                        : OptimizationCriteriaEnum.DISTANCE_KM.name());
        return info;
    }

    /* ==================== Internals ==================== */

    private Map<String, Location> loadLocationsMap() {
        Map<String, Location> map = new HashMap<>();
        for (Location l : locationRepository.findAll()) {
            map.put(l.getId(), l);
        }
        return map;
    }

    /** Estructura interna de arista (dirigida). */
    private static class Edge {
        final String from, to;
        final double distance, time, cost;
        Edge(String from, String to, double distance, double time, double cost) {
            this.from = from; this.to = to;
            this.distance = distance; this.time = time; this.cost = cost;
        }
        double weight(OptimizationCriteriaEnum c) {
            return switch (c) {
                case TIME_MIN -> time;
                case COST_FUEL -> cost;
                default -> distance;
            };
        }
    }

    /** Construye lista de adyacencia: Map<from, Map<to, Edge>> */
    private Map<String, Map<String, Edge>> buildGraphFromDb() {
        Map<String, Map<String, Edge>> g = new HashMap<>();
        List<Segment> segments = segmentRepository.findAll();

        for (Segment s : segments) {
            String from = s.getFromLocationId();
            String to   = s.getToLocationId();
            double d = nz(s.getDistanceKm());
            double t = nz(s.getTimeMin());
            double c = nz(s.getCostFuel());

            Edge e = new Edge(from, to, d, t, c);
            g.computeIfAbsent(from, k -> new HashMap<>()).put(to, e);

            if (Boolean.TRUE.equals(s.getBidirectional())) {
                Edge rev = new Edge(to, from, d, t, c);
                g.computeIfAbsent(to, k -> new HashMap<>()).put(from, rev);
            }
        }
        return g;
    }

    private static double nz(Double v) { return v == null ? 0.0 : v; }

    /** Conjunto de nodos presentes en el grafo (from ∪ to). */
    private static Set<String> allNodeIds(Map<String, Map<String, Edge>> g) {
        Set<String> ids = new HashSet<>(g.keySet());
        for (Map<String, Edge> map : g.values()) ids.addAll(map.keySet());
        return ids;
    }

    private static List<String> reconstruct(Map<String, String> prev, String s, String t) {
        if (Objects.equals(s, t)) return Collections.singletonList(s);
        LinkedList<String> path = new LinkedList<>();
        String cur = t;
        while (cur != null && !Objects.equals(cur, s)) {
            path.addFirst(cur);
            cur = prev.get(cur);
        }
        if (cur == null) return Collections.emptyList();
        path.addFirst(s);
        return path;
    }

    private RouteResponseDTO error(String msg) {
        return new RouteResponseDTO(
                Collections.emptyList(),
                Collections.emptyList(),
                0.0, 0.0, 0.0,
                AlgorithmEnum.DIJKSTRA,
                0L,
                false,
                msg
        );
    }
}
