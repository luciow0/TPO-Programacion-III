package org.example.tpoprogramacioniii.service.impl;

import org.example.tpoprogramacioniii.model.Location;
import org.example.tpoprogramacioniii.model.Segment;
import org.example.tpoprogramacioniii.repository.LocationRepository;
import org.example.tpoprogramacioniii.service.KruskalServiceI;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class KruskalServiceImpl implements KruskalServiceI {

    private final LocationRepository locationRepository;

    public KruskalServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Map<String, Object> calculateMST() {
        // 1. Obtener todos los nodos y aristas
        List<Location> locations = locationRepository.findAll();
        List<Edge> edges = new ArrayList<>();

        for (Location loc : locations) {
            for (Segment seg : loc.getSegments()) {
                if (seg.getToLocation() != null && seg.getDistanceKm() != null) {
                    edges.add(new Edge(loc.getId(), seg.getToLocation().getId(), seg.getDistanceKm()));
                }
            }
        }

        // 2. Ordenar las aristas por peso (distancia)
        edges.sort(Comparator.comparingDouble(e -> e.weight));

        // 3. Inicializar Union-Find
        UnionFind uf = new UnionFind(locations.size());
        Map<String, Integer> nodeIndex = new HashMap<>();
        for (int i = 0; i < locations.size(); i++) {
            nodeIndex.put(locations.get(i).getId(), i);
        }

        // 4. Seleccionar aristas
        List<Edge> mstEdges = new ArrayList<>();
        double totalWeight = 0;

        for (Edge edge : edges) {
            int u = nodeIndex.get(edge.from);
            int v = nodeIndex.get(edge.to);

            if (uf.find(u) != uf.find(v)) {
                uf.union(u, v);
                mstEdges.add(edge);
                totalWeight += edge.weight;
            }
        }

        // 5. Devolver resultado
        Map<String, Object> result = new HashMap<>();
        result.put("edges", mstEdges);
        result.put("totalWeight", totalWeight);
        return result;
    }

    /** Clase auxiliar para representar aristas */
    private static class Edge {
        String from;
        String to;
        double weight;

        Edge(String from, String to, double weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return String.format("%s -> %s (%.2f km)", from.substring(0,7), to.substring(0,7), weight);
        }
    }

    /** Implementación Union-Find (Disjoint Set Union) */
    private static class UnionFind {
        private final int[] parent;
        private final int[] rank;

        UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }

        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) return;

            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
        }
    }
}
