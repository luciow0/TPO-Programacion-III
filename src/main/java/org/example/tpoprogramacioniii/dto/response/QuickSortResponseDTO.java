package org.example.tpoprogramacioniii.dto.response;

import org.example.tpoprogramacioniii.Enum.AlgorithmEnum;

import java.util.List;

// Respuesta para el algoritmo de QuickSort
// Contiene los elementos ordenados y metadatos del proceso de ordenamiento

public class QuickSortResponseDTO<T> {

    private List<T> sortedElements;
    // Lista de elementos ordenados

    private AlgorithmEnum algorithm;
    // Algoritmo utilizado (QUICK_SORT)

    private Long executionTimeMs;
    // Tiempo de ejecución en milisegundos

    private Integer totalElements;
    // Número total de elementos procesados

    private Integer comparisons;
    // Número de comparaciones realizadas

    private Integer swaps;
    // Número de intercambios realizados

    private String sortCriteria;
    // Criterio utilizado para el ordenamiento

    private Boolean ascendingOrder;
    // Si el ordenamiento fue ascendente o descendente

    // Constructor vacío
    public QuickSortResponseDTO() {}

    // Constructor básico
    public QuickSortResponseDTO(List<T> sortedElements, AlgorithmEnum algorithm,
                                Long executionTimeMs, String sortCriteria, Boolean ascendingOrder) {
        this.sortedElements = sortedElements;
        this.algorithm = algorithm;
        this.executionTimeMs = executionTimeMs;
        this.sortCriteria = sortCriteria;
        this.ascendingOrder = ascendingOrder;
        this.totalElements = sortedElements != null ? sortedElements.size() : 0;
    }

    // Constructor completo
    public QuickSortResponseDTO(List<T> sortedElements, AlgorithmEnum algorithm,
                                Long executionTimeMs, Integer totalElements,
                                Integer comparisons, Integer swaps, String sortCriteria,
                                Boolean ascendingOrder) {
        this.sortedElements = sortedElements;
        this.algorithm = algorithm;
        this.executionTimeMs = executionTimeMs;
        this.totalElements = totalElements;
        this.comparisons = comparisons;
        this.swaps = swaps;
        this.sortCriteria = sortCriteria;
        this.ascendingOrder = ascendingOrder;
    }

    // Getters y Setters
    public List<T> getSortedElements() {
        return sortedElements;
    }

    public void setSortedElements(List<T> sortedElements) {
        this.sortedElements = sortedElements;
    }

    public AlgorithmEnum getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(AlgorithmEnum algorithm) {
        this.algorithm = algorithm;
    }

    public Long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public void setExecutionTimeMs(Long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }

    public Integer getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Integer totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getComparisons() {
        return comparisons;
    }

    public void setComparisons(Integer comparisons) {
        this.comparisons = comparisons;
    }

    public Integer getSwaps() {
        return swaps;
    }

    public void setSwaps(Integer swaps) {
        this.swaps = swaps;
    }

    public String getSortCriteria() {
        return sortCriteria;
    }

    public void setSortCriteria(String sortCriteria) {
        this.sortCriteria = sortCriteria;
    }

    public Boolean getAscendingOrder() {
        return ascendingOrder;
    }

    public void setAscendingOrder(Boolean ascendingOrder) {
        this.ascendingOrder = ascendingOrder;
    }
}
