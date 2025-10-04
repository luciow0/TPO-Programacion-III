package org.example.tpoprogramacioniii.service.impl;

import org.example.tpoprogramacioniii.Enum.OptimizationCriteriaEnum;
import org.example.tpoprogramacioniii.model.Location;
import org.example.tpoprogramacioniii.model.Segment;
import org.example.tpoprogramacioniii.repository.LocationRepository;
import org.example.tpoprogramacioniii.service.DijkstraServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.Math.round;

@Service
public class DijkstraServiceImpl implements DijkstraServiceI {

    private final LocationRepository locationRepository;

    @Autowired
    public DijkstraServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Map<String, Object> calculateOptimalRoute(String originLocationId, String destinationLocationId,
                                                     OptimizationCriteriaEnum criteria) {
        List<Location> all = locationRepository.findAll();
        Map<String, Location> nodes = all.stream()
                .collect(Collectors.toMap(Location::getId, Function.identity()));

        if (!nodes.containsKey(originLocationId) || !nodes.containsKey(destinationLocationId)) {
            return Map.of("valid", false, "message", "Nodo no encontrado");
        }

        // Construcción del grafo
        record Edge(String to, double w, Segment raw) {}
        Map<String, List<Edge>> adj = new HashMap<>();
        for (Location u : all) {
            if (u.getSegments() == null) continue;
            List<Edge> list = new ArrayList<>();
            for (Segment s : u.getSegments()) {
                if (s.getToLocation() == null) continue;
                double w = switch (criteria) {
                    case TIME_MIN -> s.getTimeMin() != null ? s.getTimeMin() : 0.0;
                    case COST_FUEL -> s.getCostFuel() != null ? s.getCostFuel() : 0.0;
                    case DISTANCE_KM, SEGMENTS_COUNT -> s.getDistanceKm() != null ? s.getDistanceKm() : 0.0;
                };
                list.add(new Edge(s.getToLocation().getId(), w, s));
            }
            adj.put(u.getId(), list);
        }

        // Dijkstra
        Map<String, Double> dist = new HashMap<>();
        Map<String, String> parent = new HashMap<>();
        for (String id : nodes.keySet()) dist.put(id, Double.POSITIVE_INFINITY);
        dist.put(originLocationId, 0.0);

        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));
        pq.add(originLocationId);

        while (!pq.isEmpty()) {
            String u = pq.poll();
            if (u.equals(destinationLocationId)) break;

            for (Edge e : adj.getOrDefault(u, List.of())) {
                double nd = dist.get(u) + e.w();
                if (nd < dist.get(e.to())) {
                    dist.put(e.to(), nd);
                    parent.put(e.to(), u);
                    pq.add(e.to());
                }
            }
        }

        if (!parent.containsKey(destinationLocationId) && !originLocationId.equals(destinationLocationId)) {
            return Map.of("valid", false, "message", "No hay ruta");
        }

        // Reconstruir camino
        List<String> path = new ArrayList<>();
        for (String cur = destinationLocationId; cur != null; cur = parent.get(cur)) {
            path.add(cur);
        }
        Collections.reverse(path);

        return Map.of(
                "valid", true,
                "message", "OK",
                "path", path,
                "distance", roundDouble(dist.get(destinationLocationId))
        );
    }
    private static double roundDouble(double value) {
        double factor = Math.pow(10, 2);
        return Math.round(value * factor) / factor;
    }

}
