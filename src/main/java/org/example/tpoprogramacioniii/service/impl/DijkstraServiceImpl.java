package org.example.tpoprogramacioniii.service.impl;

import org.example.tpoprogramacioniii.dto.request.RouteRequestDTO;
import org.example.tpoprogramacioniii.dto.response.LocationResponseDTO;
import org.example.tpoprogramacioniii.dto.response.RouteResponseDTO;
import org.example.tpoprogramacioniii.dto.response.SegmentResponseDTO;
import org.example.tpoprogramacioniii.Enum.AlgorithmEnum;
import org.example.tpoprogramacioniii.Enum.OptimizationCriteriaEnum;
import org.example.tpoprogramacioniii.service.DijkstraServiceI;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DijkstraServiceImpl implements DijkstraServiceI {

    // Grafo representado como lista de adyacencia
    private Map<String, Map<String, Edge>> graph;

    public DijkstraServiceImpl() {
        this.graph = new HashMap<>();
    }

    // Clase interna para representar aristas
    private static class Edge {
        String from, to;
        double distance;
        double time;
        double cost;

        Edge(String from, String to, double distance, double time, double cost) {
            this.from = from;
            this.to = to;
            this.distance = distance;
            this.time = time;
            this.cost = cost;
        }
    }

    // Clase interna para nodos en la cola de prioridad
    private static class Node implements Comparable<Node> {
        String id;
        double distance;
        String previous;

        Node(String id, double distance, String previous) {
            this.id = id;
            this.distance = distance;
            this.previous = previous;
        }

        @Override
        public int compareTo(Node other) {
            return Double.compare(this.distance, other.distance);
        }
    }

    @Override
    public RouteResponseDTO calculateOptimalRoute(RouteRequestDTO request) {
        long startTime = System.currentTimeMillis();

        try {
            String originId = request.getOriginLocationId();
            String destinationId = request.getDestinationLocationId();
            OptimizationCriteriaEnum criteria = request.getOptimizationCriteria();

            // Verificar que existen los nodos
            if (!graph.containsKey(originId) || !graph.containsKey(destinationId)) {
                return createErrorResponse("Una o ambas ubicaciones no existen en el grafo");
            }

            // Ejecutar Dijkstra
            Map<String, String> previous = dijkstra(originId, criteria);

            // Reconstruir la ruta
            List<String> path = reconstructPath(originId, destinationId, previous);

            if (path.isEmpty()) {
                return createErrorResponse("No se encontró una ruta válida entre las ubicaciones");
            }

            // Calcular métricas totales
            double totalDistance = 0;
            double totalTime = 0;
            double totalCost = 0;
            List<SegmentResponseDTO> segments = new ArrayList<>();

            for (int i = 0; i < path.size() - 1; i++) {
                String from = path.get(i);
                String to = path.get(i + 1);
                Edge edge = graph.get(from).get(to);

                totalDistance += edge.distance;
                totalTime += edge.time;
                totalCost += edge.cost;

                segments.add(new SegmentResponseDTO(from, to, edge.distance, edge.time,
                        edge.cost, "HIGHWAY", 100.0));
            }

            // Convertir path a LocationResponseDTO
            List<LocationResponseDTO> locationPath = new ArrayList<>();
            for (String locationId : path) {
                locationPath.add(new LocationResponseDTO(locationId, "Location " + locationId,
                        0.0, 0.0, "Address " + locationId));
            }

            long executionTime = System.currentTimeMillis() - startTime;

            return new RouteResponseDTO(locationPath, segments, totalDistance, totalTime,
                    totalCost, AlgorithmEnum.DIJKSTRA, executionTime, true, "Ruta calculada exitosamente");

        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            return new RouteResponseDTO(null, null, 0, 0, 0,
                    AlgorithmEnum.DIJKSTRA, executionTime, false, "Error: " + e.getMessage());
        }
    }

    @Override
    public RouteResponseDTO calculateShortestDistanceRoute(String originLocationId, String destinationLocationId) {
        RouteRequestDTO request = new RouteRequestDTO(originLocationId, destinationLocationId,
                OptimizationCriteriaEnum.DISTANCE);
        return calculateOptimalRoute(request);
    }

    @Override
    public RouteResponseDTO calculateFastestTimeRoute(String originLocationId, String destinationLocationId) {
        RouteRequestDTO request = new RouteRequestDTO(originLocationId, destinationLocationId,
                OptimizationCriteriaEnum.TIME);
        return calculateOptimalRoute(request);
    }

    @Override
    public RouteResponseDTO calculateCheapestCostRoute(String originLocationId, String destinationLocationId) {
        RouteRequestDTO request = new RouteRequestDTO(originLocationId, destinationLocationId,
                OptimizationCriteriaEnum.COST);
        return calculateOptimalRoute(request);
    }

    @Override
    public List<RouteResponseDTO> calculateAllRoutesFromOrigin(String originLocationId) {
        List<RouteResponseDTO> routes = new ArrayList<>();

        if (!graph.containsKey(originLocationId)) {
            return routes;
        }

        // Ejecutar Dijkstra desde el origen
        Map<String, String> previous = dijkstra(originLocationId, OptimizationCriteriaEnum.DISTANCE);

        // Crear rutas a todos los destinos alcanzables
        for (String destinationId : graph.keySet()) {
            if (!destinationId.equals(originLocationId)) {
                List<String> path = reconstructPath(originLocationId, destinationId, previous);
                if (!path.isEmpty()) {
                    RouteRequestDTO request = new RouteRequestDTO(originLocationId, destinationId,
                            OptimizationCriteriaEnum.DISTANCE);
                    routes.add(calculateOptimalRoute(request));
                }
            }
        }

        return routes;
    }

    @Override
    public boolean hasValidRoute(String originLocationId, String destinationLocationId) {
        if (!graph.containsKey(originLocationId) || !graph.containsKey(destinationLocationId)) {
            return false;
        }

        Map<String, String> previous = dijkstra(originLocationId, OptimizationCriteriaEnum.DISTANCE);
        List<String> path = reconstructPath(originLocationId, destinationLocationId, previous);

        return !path.isEmpty();
    }

    @Override
    public Map<String, Object> getAlgorithmPerformanceInfo(RouteRequestDTO request) {
        Map<String, Object> info = new HashMap<>();

        long startTime = System.currentTimeMillis();
        RouteResponseDTO result = calculateOptimalRoute(request);
        long endTime = System.currentTimeMillis();

        info.put("executionTimeMs", endTime - startTime);
        info.put("nodesVisited", graph.size());
        info.put("algorithm", "DIJKSTRA");
        info.put("optimizationCriteria", request.getOptimizationCriteria());
        info.put("routeFound", result.isValid());
        info.put("totalDistance", result.getTotalDistanceKm());
        info.put("totalTime", result.getTotalTimeMin());
        info.put("totalCost", result.getTotalCost());

        return info;
    }

    // Método principal del algoritmo de Dijkstra
    private Map<String, String> dijkstra(String start, OptimizationCriteriaEnum criteria) {
        Map<String, Double> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<Node> queue = new PriorityQueue<>();
        Set<String> visited = new HashSet<>();

        // Inicializar distancias
        for (String node : graph.keySet()) {
            distances.put(node, Double.POSITIVE_INFINITY);
        }
        distances.put(start, 0.0);
        queue.add(new Node(start, 0.0, null));

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (visited.contains(current.id)) {
                continue;
            }

            visited.add(current.id);

            // Explorar vecinos
            if (graph.containsKey(current.id)) {
                for (Map.Entry<String, Edge> entry : graph.get(current.id).entrySet()) {
                    String neighbor = entry.getKey();
                    Edge edge = entry.getValue();

                    if (!visited.contains(neighbor)) {
                        double weight = getWeight(edge, criteria);
                        double newDistance = distances.get(current.id) + weight;

                        if (newDistance < distances.get(neighbor)) {
                            distances.put(neighbor, newDistance);
                            previous.put(neighbor, current.id);
                            queue.add(new Node(neighbor, newDistance, current.id));
                        }
                    }
                }
            }
        }

        return previous;
    }

    // Obtener peso según el criterio de optimización
    private double getWeight(Edge edge, OptimizationCriteriaEnum criteria) {
        switch (criteria) {
            case DISTANCE:
                return edge.distance;
            case TIME:
                return edge.time;
            case COST:
                return edge.cost;
            default:
                return edge.distance;
        }
    }

    // Reconstruir la ruta desde el destino hasta el origen
    private List<String> reconstructPath(String start, String end, Map<String, String> previous) {
        List<String> path = new ArrayList<>();
        String current = end;

        while (current != null) {
            path.add(current);
            current = previous.get(current);
        }

        Collections.reverse(path);

        // Verificar que el camino comience en el origen
        if (!path.isEmpty() && !path.get(0).equals(start)) {
            return new ArrayList<>();
        }

        return path;
    }

    // Crear respuesta de error
    private RouteResponseDTO createErrorResponse(String message) {
        return new RouteResponseDTO(null, null, 0, 0, 0,
                AlgorithmEnum.DIJKSTRA, 0L, false, message);
    }

    // Método para agregar aristas al grafo (útil para testing o inicialización)
    public void addEdge(String from, String to, double distance, double time, double cost) {
        graph.computeIfAbsent(from, k -> new HashMap<>()).put(to, new Edge(from, to, distance, time, cost));
        graph.computeIfAbsent(to, k -> new HashMap<>()).put(from, new Edge(to, from, distance, time, cost));
    }

    // Método para agregar nodos al grafo
    public void addNode(String nodeId) {
        graph.putIfAbsent(nodeId, new HashMap<>());
    }
}
