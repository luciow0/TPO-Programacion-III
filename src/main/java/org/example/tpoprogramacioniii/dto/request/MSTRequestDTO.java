package org.example.tpoprogramacioniii.dto.request;

// Se usa para solicitar la red de infraestructura mínima.
// (Para Prim y Kruskal)

import org.example.tpoprogramacioniii.Enum.AreaEnum;
import org.example.tpoprogramacioniii.Enum.OptimizationCriteriaEnum;

public class MSTRequestDTO {
    private AreaEnum areaOfInterest;
    // Nombre o ID de la región donde se aplicará el algoritmo.
    private OptimizationCriteriaEnum optimizationCriteria;
    // Métrica para calcular el MST: "DISTANCE", "COST".

    public MSTRequestDTO(AreaEnum areaOfInterest, OptimizationCriteriaEnum optimizationCriteria) {
        this.areaOfInterest = areaOfInterest;
        this.optimizationCriteria = optimizationCriteria;
    }

    public MSTRequestDTO() {}

    public AreaEnum getAreaOfInterest() {
        return areaOfInterest;
    }

    public void setAreaOfInterest(AreaEnum areaOfInterest) {
        this.areaOfInterest = areaOfInterest;
    }

    public OptimizationCriteriaEnum getOptimizationCriteria() {
        return optimizationCriteria;
    }

    public void setOptimizationCriteria(OptimizationCriteriaEnum optimizationCriteria) {
        this.optimizationCriteria = optimizationCriteria;
    }
}
