package org.example.tpoprogramacioniii.model;

// son las relaciones o aristas entre los nodos
// debe encapsular toda la informacion critica en ese tramo de red
import org.springframework.data.neo4j.core.schema.*;

@RelationshipProperties
public class Segment {

    @Id
    @GeneratedValue // generatedvalue le dice a SDN
    private Long id;

    @TargetNode
    private Location toLocation;

    private Double distanceKm;
    private Double timeMin;
    private Double costFuel;

    public Segment(Location toLocation, Double distanceKm, Double timeMin, Double costFuel) {
        this.toLocation = toLocation;
        this.distanceKm = distanceKm;
        this.timeMin = timeMin;
        this.costFuel = costFuel;
    }

    public Segment(){}

    public Location getToLocation() {
        return toLocation;
    }

    public void setToLocation(Location toLocation) {
        this.toLocation = toLocation;
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
}
