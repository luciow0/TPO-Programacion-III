package org.example.tpoprogramacioniii.dto.response;

// Respuesta para cualquier algoritmo que devuelva una
// secuencia de nodos/aristas.
//  (Para Dijkstra, BFS, DFS, TSP, DP, Backtracking)

import org.example.tpoprogramacioniii.Enum.AlgorithmEnum;

import java.util.List;

public class RouteResponseDTO {
    private List<LocationResponseDTO> path;
    // Secuencia ordenada de los nodos visitados (la ruta).
    private List<SegmentResponseDTO> segmentsUsed;
    // Lista de los segmentos de la carretera que componen la ruta.
    private double totalDistanceKm;
    // Suma total de las distancias de los segmentos
    private double totalTimeMin;
    // Suma total de los tiempos de los segmentos
    private AlgorithmEnum algorithm; // Nombre del algoritmo utilizado (ej. "DIJKSTRA", "A_STAR").

    private double totalCost;
    // Costo total de la ruta

    private Long executionTimeMs;
    // Tiempo de ejecución del algoritmo en milisegundos

    private boolean isValid;
    // Indica si se encontró una ruta válida

    private String message;
    // Mensaje informativo sobre el resultado

    // Constructor vacío
    public RouteResponseDTO() {}

    // Constructor básico
    public RouteResponseDTO(List<LocationResponseDTO> path, List<SegmentResponseDTO> segmentsUsed,
                            double totalDistanceKm, double totalTimeMin, AlgorithmEnum algorithm) {
        this.path = path;
        this.segmentsUsed = segmentsUsed;
        this.totalDistanceKm = totalDistanceKm;
        this.totalTimeMin = totalTimeMin;
        this.algorithm = algorithm;
        this.isValid = path != null && !path.isEmpty();
    }

    // Constructor completo
    public RouteResponseDTO(List<LocationResponseDTO> path, List<SegmentResponseDTO> segmentsUsed,
                            double totalDistanceKm, double totalTimeMin, double totalCost,
                            AlgorithmEnum algorithm, Long executionTimeMs, boolean isValid, String message) {
        this.path = path;
        this.segmentsUsed = segmentsUsed;
        this.totalDistanceKm = totalDistanceKm;
        this.totalTimeMin = totalTimeMin;
        this.totalCost = totalCost;
        this.algorithm = algorithm;
        this.executionTimeMs = executionTimeMs;
        this.isValid = isValid;
        this.message = message;
    }

    // Getters y Setters
    public List<LocationResponseDTO> getPath() {
        return path;
    }

    public void setPath(List<LocationResponseDTO> path) {
        this.path = path;
    }

    public List<SegmentResponseDTO> getSegmentsUsed() {
        return segmentsUsed;
    }

    public void setSegmentsUsed(List<SegmentResponseDTO> segmentsUsed) {
        this.segmentsUsed = segmentsUsed;
    }

    public double getTotalDistanceKm() {
        return totalDistanceKm;
    }

    public void setTotalDistanceKm(double totalDistanceKm) {
        this.totalDistanceKm = totalDistanceKm;
    }

    public double getTotalTimeMin() {
        return totalTimeMin;
    }

    public void setTotalTimeMin(double totalTimeMin) {
        this.totalTimeMin = totalTimeMin;
    }

    public AlgorithmEnum getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(AlgorithmEnum algorithm) {
        this.algorithm = algorithm;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public Long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public void setExecutionTimeMs(Long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
