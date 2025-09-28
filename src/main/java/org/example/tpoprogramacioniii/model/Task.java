package org.example.tpoprogramacioniii.model;

// Representa un trabajo que debe realizarse
// en una ubicación (ej. una entrega, una recogida).

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.sql.Time;

@Node
public class Task {
    @Id
    private String id;
    private Integer priority;
    private Time time_window_start;
    private Time time_window_end;

    public Task(String id, Integer priority, Time time_window_start, Time time_window_end) {
        this.id = id;
        this.priority = priority;
        this.time_window_start = time_window_start;
        this.time_window_end = time_window_end;
    }

    public Task(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Time getTime_window_start() {
        return time_window_start;
    }

    public void setTime_window_start(Time time_window_start) {
        this.time_window_start = time_window_start;
    }

    public Time getTime_window_end() {
        return time_window_end;
    }

    public void setTime_window_end(Time time_window_end) {
        this.time_window_end = time_window_end;
    }
}
