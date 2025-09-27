package org.example.tpoprogramacioniii.dto.request;

// Se usa para encontrar el camino entre dos puntos.
// (Para Dijkstra, BFS, DFS)

import org.example.tpoprogramacioniii.Enum.OptimizationCriteriaEnum;
import org.springframework.data.annotation.Id;

public class RouteRequestDTO {

    @Id
    private String originLocationId;
    // ID único del nodo de inicio
    @Id
    private String destinationLocationId;
    // ID único del nodo de destino
    private OptimizationCriteriaEnum optimizationCriteria;
    // Métrica a optimizar: "TIME", "DISTANCE", "COST".

    public RouteRequestDTO(String originLocationId, String destinationLocationId, OptimizationCriteriaEnum optimizationCriteria) {
        this.originLocationId = originLocationId;
        this.destinationLocationId = destinationLocationId;
        this.optimizationCriteria = optimizationCriteria;
    }

    public RouteRequestDTO() {}

    public String getOriginLocationId() {
        return originLocationId;
    }

    public void setOriginLocationId(String originLocationId) {
        this.originLocationId = originLocationId;
    }

    public String getDestinationLocationId() {
        return destinationLocationId;
    }

    public void setDestinationLocationId(String destinationLocationId) {
        this.destinationLocationId = destinationLocationId;
    }

    public OptimizationCriteriaEnum getOptimizationCriteria() {
        return optimizationCriteria;
    }

    public void setOptimizationCriteria(OptimizationCriteriaEnum optimizationCriteria) {
        this.optimizationCriteria = optimizationCriteria;
    }
}
