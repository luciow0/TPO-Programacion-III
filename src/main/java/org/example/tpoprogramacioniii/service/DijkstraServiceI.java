package org.example.tpoprogramacioniii.service;

import org.example.tpoprogramacioniii.Enum.OptimizationCriteriaEnum;

import java.util.Map;

public interface DijkstraServiceI {

    /**
     * Calcula la ruta óptima entre dos ubicaciones según el criterio indicado.
     * Retorna un Map con:
     *  - "valid": boolean
     *  - "message": String
     *  - "path": List<String> (ids de Location en orden)
     *  - "distance": double (costo total según el criterio)
     */
    Map<String, Object> calculateOptimalRoute(String originLocationId,
                                              String destinationLocationId,
                                              OptimizationCriteriaEnum criteria);
}
