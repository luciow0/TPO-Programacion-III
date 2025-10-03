package org.example.tpoprogramacioniii.service;

import java.util.List;
import java.util.Map;

public interface DepthFirstSearchServiceI {
    /**
     * Ejecuta una búsqueda en profundidad entre dos nodos.
     * @param originId ID del nodo origen
     * @param destId ID del nodo destino
     * @return Mapa con claves:
     *   "valid" -> Boolean (true si encontró camino)
     *   "path" -> List<String> con los IDs de los nodos visitados en el camino
     *   "message" -> String con explicación si no hay camino
     */
    Map<String, Object> depthFirstSearch(String originId, String destId);
}
