package org.example.tpoprogramacioniii.service.impl;

import org.example.tpoprogramacioniii.model.Location;
import org.example.tpoprogramacioniii.model.Segment;
import org.example.tpoprogramacioniii.repository.LocationRepository;
import org.example.tpoprogramacioniii.service.DepthFirstSearchServiceI;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DepthFirstSearchServiceImpl implements DepthFirstSearchServiceI {

    private final LocationRepository locationRepository;

    public DepthFirstSearchServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Map<String, Object> depthFirstSearch(String originId, String destId) {
        Map<String, Object> result = new HashMap<>();

        Optional<Location> originOpt = locationRepository.findById(originId);
        Optional<Location> destOpt = locationRepository.findById(destId);

        if (originOpt.isEmpty() || destOpt.isEmpty()) {
            result.put("valid", false);
            result.put("message", "Origen o destino no encontrados en el grafo.");
            return result;
        }

        Location origin = originOpt.get();
        Location dest = destOpt.get();

        Set<String> visited = new HashSet<>();
        List<String> path = new ArrayList<>();

        boolean found = dfs(origin, destId, visited, path);

        if (!found) {
            result.put("valid", false);
            result.put("message", "No existe camino entre los nodos seleccionados.");
            return result;
        }

        result.put("valid", true);
        result.put("path", path);
        return result;
    }

    private boolean dfs(Location current, String destId, Set<String> visited, List<String> path) {
        visited.add(current.getId());
        path.add(current.getId());

        if (current.getId().equals(destId)) {
            return true;
        }

        if (current.getSegments() != null) {
            for (Segment seg : current.getSegments()) {
                Location neighbor = seg.getToLocation();
                if (neighbor != null && !visited.contains(neighbor.getId())) {
                    boolean found = dfs(neighbor, destId, visited, path);
                    if (found) return true;
                }
            }
        }

        // backtrack
        path.remove(path.size() - 1);
        return false;
    }
}
