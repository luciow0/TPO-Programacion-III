package org.example.tpoprogramacioniii.service.impl;

import org.example.tpoprogramacioniii.model.Location;
import org.example.tpoprogramacioniii.model.Segment;
import org.example.tpoprogramacioniii.repository.LocationRepository;
import org.example.tpoprogramacioniii.service.BreadthFirstSearchServiceI;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BreadthFirstSearchServiceImpl implements BreadthFirstSearchServiceI {

    private final LocationRepository locationRepository;

    public BreadthFirstSearchServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    /**
     * BFS clásico desde un nodo origen.
     * Devuelve la lista de IDs de nodos en el orden en que fueron visitados.
     */
    public List<String> bfs(String originId) {
        Optional<Location> startOpt = locationRepository.findById(originId);
        if (startOpt.isEmpty()) {
            return Collections.emptyList();
        }

        Location start = startOpt.get();
        List<String> visitedOrder = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Queue<Location> queue = new LinkedList<>();

        visited.add(start.getId());
        queue.add(start);

        while (!queue.isEmpty()) {
            Location current = queue.poll();
            visitedOrder.add(current.getId());

            // recorrer los segmentos salientes
            if (current.getSegments() != null) {
                for (Segment seg : current.getSegments()) {
                    Location neighbor = seg.getToLocation();
                    if (neighbor != null && !visited.contains(neighbor.getId())) {
                        visited.add(neighbor.getId());
                        queue.add(neighbor);
                    }
                }
            }
        }
        return visitedOrder;
    }
}
