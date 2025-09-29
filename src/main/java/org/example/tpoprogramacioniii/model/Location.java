package org.example.tpoprogramacioniii.model;

// Es el nodo fundamental. Representa cualquier punto geográfico en la red.

import org.example.tpoprogramacioniii.Enum.AreaEnum;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Node
public class Location {
    @Id
    private String id;
    private String name;
    private AreaEnum area;

    @Relationship(type = "CONNECTED_TO", direction = Relationship.Direction.OUTGOING)
    private List<Segment> segments = new ArrayList<>();

    public Location(String id, String name, AreaEnum area) {
        this.id = id;
        this.name = name;
        this.area = area;
    }

    public Location(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AreaEnum getArea() {
        return area;
    }

    public void setArea(AreaEnum area) {
        this.area = area;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }
}
