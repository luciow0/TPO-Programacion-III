package org.example.tpoprogramacioniii.service;

import org.example.tpoprogramacioniii.model.Segment;

import java.util.List;
import java.util.Map;

public interface KruskalServiceI {
    /**
     * Calcula el MST con Kruskal y devuelve:
     * - "edges": lista de segmentos seleccionados
     * - "totalWeight": peso total del MST
     */
    Map<String, Object> calculateMST();
}
