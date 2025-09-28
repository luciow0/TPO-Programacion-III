package org.example.tpoprogramacioniii.dto.response;

// Entidad base. Se usa para representar una arista en las repuestas

public class SegmentResponseDTO {
    private String fromLocationId;
    private String toLocationId;
    private double distanceKm;
    private double timeMin;
    private double cost;
    private String roadType;
    private double speedLimit;

    // Constructor vacío
    public SegmentResponseDTO() {}

    // Constructor básico
    public SegmentResponseDTO(String fromLocationId, String toLocationId, double distanceKm, double timeMin) {
        this.fromLocationId = fromLocationId;
        this.toLocationId = toLocationId;
        this.distanceKm = distanceKm;
        this.timeMin = timeMin;
    }

    // Constructor completo
    public SegmentResponseDTO(String fromLocationId, String toLocationId, double distanceKm,
                              double timeMin, double cost, String roadType, double speedLimit) {
        this.fromLocationId = fromLocationId;
        this.toLocationId = toLocationId;
        this.distanceKm = distanceKm;
        this.timeMin = timeMin;
        this.cost = cost;
        this.roadType = roadType;
        this.speedLimit = speedLimit;
    }

    // Getters y Setters
    public String getFromLocationId() {
        return fromLocationId;
    }

    public void setFromLocationId(String fromLocationId) {
        this.fromLocationId = fromLocationId;
    }

    public String getToLocationId() {
        return toLocationId;
    }

    public void setToLocationId(String toLocationId) {
        this.toLocationId = toLocationId;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public double getTimeMin() {
        return timeMin;
    }

    public void setTimeMin(double timeMin) {
        this.timeMin = timeMin;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getRoadType() {
        return roadType;
    }

    public void setRoadType(String roadType) {
        this.roadType = roadType;
    }

    public double getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(double speedLimit) {
        this.speedLimit = speedLimit;
    }
}
