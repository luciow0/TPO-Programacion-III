package org.example.tpoprogramacioniii.service;

import java.util.List;
import java.util.Map;

public interface PrimServiceI {
    /**
     * Ejecuta el algoritmo de Prim desde un nodo origen.
     *
     * @param startId ID del nodo origen en la base de datos
     * @return Un mapa con:
     *   - "valid": boolean
     *   - "message": String (en caso de error)
     *   - "mstEdges": List<String> (lista de aristas formateadas origen->destino)
     *   - "totalWeight": Double (peso total del MST)
     */
    Map<String, Object> calculateMST(String startId);
}
