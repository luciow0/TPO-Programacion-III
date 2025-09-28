package org.example.tpoprogramacioniii.model;

// son las relaciones o aristas entre los nodos
// debe encapsular toda la informacion critica en ese tramo de red
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
public class Segment {

    @Id
    private String id;
    private String fromLocationId;
    private String toLocationId;
    private Double distanceKm;
    private Double timeMin;
    private Double costFuel;
    private Boolean isBidirectional;


    public Segment(String id, String fromLocationId, String toLocationId, Double distanceKm, Double timeMin, Double costFuel, Boolean isBidirectional) {
        this.id = id;
        this.fromLocationId = fromLocationId;
        this.toLocationId = toLocationId;
        this.distanceKm = distanceKm;
        this.timeMin = timeMin;
        this.costFuel = costFuel;
        this.isBidirectional = isBidirectional;
    }

    public Segment(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public Double getTimeMin() {
        return timeMin;
    }

    public void setTimeMin(Double timeMin) {
        this.timeMin = timeMin;
    }

    public Double getCostFuel() {
        return costFuel;
    }

    public void setCostFuel(Double costFuel) {
        this.costFuel = costFuel;
    }

    public Boolean getBidirectional() {
        return isBidirectional;
    }

    public void setBidirectional(Boolean bidirectional) {
        isBidirectional = bidirectional;
    }
}
