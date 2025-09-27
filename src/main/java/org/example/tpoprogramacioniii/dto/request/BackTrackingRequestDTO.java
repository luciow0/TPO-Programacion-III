package org.example.tpoprogramacioniii.dto.request;

import org.example.tpoprogramacioniii.Enum.OptimizationCriteriaEnum;

import java.util.List;

// Se usa para el algoritmo de BackTracking
// Permite especificar restricciones y condiciones para la búsqueda de rutas

public class BackTrackingRequestDTO {

    private String originLocationId;
    // ID único del nodo de inicio

    private String destinationLocationId;
    // ID único del nodo de destino

    private OptimizationCriteriaEnum optimizationCriteria;
    // Métrica a optimizar: "TIME", "DISTANCE", "COST"

    private List<String> mandatoryLocations;
    // Lista de IDs de ubicaciones que DEBEN ser visitadas en la ruta

    private List<String> forbiddenLocations;
    // Lista de IDs de ubicaciones que NO deben ser visitadas

    private Double maxTotalTime;
    // Tiempo máximo permitido para toda la ruta (en minutos)

    private Double maxTotalDistance;
    // Distancia máxima permitida para toda la ruta (en km)

    private Double maxTotalCost;
    // Costo máximo permitido para toda la ruta

    private Integer maxStops;
    // Número máximo de paradas/intermedias permitidas

    private Boolean allowRevisits;
    // Si se permite volver a visitar ubicaciones ya visitadas

    // Constructor vacío
    public BackTrackingRequestDTO() {}

    // Constructor completo
    public BackTrackingRequestDTO(String originLocationId, String destinationLocationId,
                                  OptimizationCriteriaEnum optimizationCriteria,
                                  List<String> mandatoryLocations, List<String> forbiddenLocations,
                                  Double maxTotalTime, Double maxTotalDistance, Double maxTotalCost,
                                  Integer maxStops, Boolean allowRevisits) {
        this.originLocationId = originLocationId;
        this.destinationLocationId = destinationLocationId;
        this.optimizationCriteria = optimizationCriteria;
        this.mandatoryLocations = mandatoryLocations;
        this.forbiddenLocations = forbiddenLocations;
        this.maxTotalTime = maxTotalTime;
        this.maxTotalDistance = maxTotalDistance;
        this.maxTotalCost = maxTotalCost;
        this.maxStops = maxStops;
        this.allowRevisits = allowRevisits;
    }

    // Getters y Setters
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

    public List<String> getMandatoryLocations() {
        return mandatoryLocations;
    }

    public void setMandatoryLocations(List<String> mandatoryLocations) {
        this.mandatoryLocations = mandatoryLocations;
    }

    public List<String> getForbiddenLocations() {
        return forbiddenLocations;
    }

    public void setForbiddenLocations(List<String> forbiddenLocations) {
        this.forbiddenLocations = forbiddenLocations;
    }

    public Double getMaxTotalTime() {
        return maxTotalTime;
    }

    public void setMaxTotalTime(Double maxTotalTime) {
        this.maxTotalTime = maxTotalTime;
    }

    public Double getMaxTotalDistance() {
        return maxTotalDistance;
    }

    public void setMaxTotalDistance(Double maxTotalDistance) {
        this.maxTotalDistance = maxTotalDistance;
    }

    public Double getMaxTotalCost() {
        return maxTotalCost;
    }

    public void setMaxTotalCost(Double maxTotalCost) {
        this.maxTotalCost = maxTotalCost;
    }

    public Integer getMaxStops() {
        return maxStops;
    }

    public void setMaxStops(Integer maxStops) {
        this.maxStops = maxStops;
    }

    public Boolean getAllowRevisits() {
        return allowRevisits;
    }

    public void setAllowRevisits(Boolean allowRevisits) {
        this.allowRevisits = allowRevisits;
    }
}
