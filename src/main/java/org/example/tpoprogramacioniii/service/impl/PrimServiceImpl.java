package org.example.tpoprogramacioniii.service.impl;

import org.example.tpoprogramacioniii.model.Location;
import org.example.tpoprogramacioniii.model.Segment;
import org.example.tpoprogramacioniii.repository.LocationRepository;
import org.example.tpoprogramacioniii.service.PrimServiceI;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PrimServiceImpl implements PrimServiceI {

    private final LocationRepository locationRepository;

    public PrimServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Map<String, Object> calculateMST(String startId) {
        Map<String, Object> result = new HashMap<>();

        Optional<Location> optStart = locationRepository.findById(startId);
        if (optStart.isEmpty()) {
            result.put("valid", false);
            result.put("message", "Nodo de inicio no encontrado.");
            return result;
        }

        List<Location> allLocations = locationRepository.findAll();
        if (allLocations.isEmpty()) {
            result.put("valid", false);
            result.put("message", "No hay nodos en la base.");
            return result;
        }

        // Conjuntos de nodos incluidos en MST y no incluidos
        Set<String> inMST = new HashSet<>();
        inMST.add(startId);

        // Cola de prioridad: aristas por distancia
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingDouble(e -> e.weight));

        // Inicializar con aristas del nodo inicial
        Location start = optStart.get();
        for (Segment seg : start.getSegments()) {
            pq.add(new Edge(start.getId(), seg.getToLocation().getId(), seg.getDistanceKm()));
        }

        List<String> mstEdges = new ArrayList<>();
        double totalWeight = 0.0;

        while (!pq.isEmpty() && inMST.size() < allLocations.size()) {
            Edge edge = pq.poll();
            if (inMST.contains(edge.to)) {
                continue; // ya está conectado
            }

            // Añadir arista al MST
            inMST.add(edge.to);
            mstEdges.add(edge.from.substring(0,7) + " -> " + edge.to.substring(0,7));
            totalWeight += edge.weight;

            // Expandir con las aristas desde el nuevo nodo
            Location newNode = locationRepository.findById(edge.to).orElse(null);
            if (newNode != null) {
                for (Segment seg : newNode.getSegments()) {
                    if (!inMST.contains(seg.getToLocation().getId())) {
                        pq.add(new Edge(newNode.getId(), seg.getToLocation().getId(), seg.getDistanceKm()));
                    }
                }
            }
        }

        // Validar si conectamos todo
        if (inMST.size() != allLocations.size()) {
            result.put("valid", false);
            result.put("message", "El grafo no es conexo, no se pudo generar un MST completo.");
            result.put("mstEdges", mstEdges);
            result.put("totalWeight", totalWeight);
            return result;
        }

        result.put("valid", true);
        result.put("mstEdges", mstEdges);
        result.put("totalWeight", totalWeight);
        return result;
    }

    /** Clase interna para manejar aristas en la PQ */
    private static class Edge {
        String from;
        String to;
        double weight;

        Edge(String from, String to, double weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }
}
