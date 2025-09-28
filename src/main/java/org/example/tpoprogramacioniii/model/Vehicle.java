package org.example.tpoprogramacioniii.model;

// Representa el recurso que utiliza las rutas.

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
public class Vehicle {

    @Id
    private String id;
    private Integer capacity;
    private Location location;
    private String status;

    public Vehicle(String id, Integer capacity, Location location, String status) {
        this.id = id;
        this.capacity = capacity;
        this.location = location;
        this.status = status;
    }

    public Vehicle(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
