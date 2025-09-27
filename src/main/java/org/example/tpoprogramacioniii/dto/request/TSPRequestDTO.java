package org.example.tpoprogramacioniii.dto.request;

// Se usa para problemas de secuenciación de entregas (Vendedor Viajero).
// (Para Programación Dinámica, Backtracking, Ramificación y Poda)

import org.springframework.data.annotation.Id;

import java.util.List;

public class TSPRequestDTO {
    @Id
    private String vehicleId;
    // ID del vehículo/repartidor que realizará las tareas.
    // Define el punto de partida (depósito).
    private List<String> taskIds;
    // Lista de IDs de las tareas (:Task) que deben ser visitadas
    private boolean returnToOrigin;
    // Indica si la ruta debe terminar donde empezó (true para TSP, false para Pathfinding simple).
    private double maxDurationHours;
    // Restricción de tiempo máximo para Backtracking/Poda.
    // Ramificación y Poda: Podar ramas que superen esta duración.


    public TSPRequestDTO(String vehicleId, List<String> taskIds, boolean returnToOrigin, double maxDurationHours) {
        this.vehicleId = vehicleId;
        this.taskIds = taskIds;
        this.returnToOrigin = returnToOrigin;
        this.maxDurationHours = maxDurationHours;
    }

    public TSPRequestDTO(){}

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public List<String> getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(List<String> taskIds) {
        this.taskIds = taskIds;
    }

    public boolean isReturnToOrigin() {
        return returnToOrigin;
    }

    public void setReturnToOrigin(boolean returnToOrigin) {
        this.returnToOrigin = returnToOrigin;
    }

    public double getMaxDurationHours() {
        return maxDurationHours;
    }

    public void setMaxDurationHours(double maxDurationHours) {
        this.maxDurationHours = maxDurationHours;
    }
}
