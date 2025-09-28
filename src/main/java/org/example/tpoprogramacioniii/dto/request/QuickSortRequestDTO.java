package org.example.tpoprogramacioniii.dto.request;

import org.example.tpoprogramacioniii.Enum.OptimizationCriteriaEnum;

import java.util.List;

// Se usa para el algoritmo de QuickSort
// Permite ordenar elementos por diferentes criterios

public class QuickSortRequestDTO<T> {
    
    private List<T> elementsToSort;
    // Lista de elementos a ordenar (pueden ser ubicaciones, tareas, vehículos, etc.)
    
    private OptimizationCriteriaEnum sortCriteria;
    // Criterio de ordenamiento: "TIME", "DISTANCE", "COST"
    
    private String sortField;
    // Campo específico por el cual ordenar (ej: "priority", "distance", "createdAt")
    
    private Boolean ascendingOrder;
    // true para orden ascendente, false para descendente
    
    private Integer limit;
    // Número máximo de elementos a retornar (opcional)
    
    // Constructor vacío
    public QuickSortRequestDTO() {}
    
    // Constructor básico
    public QuickSortRequestDTO(List<T> elementsToSort, OptimizationCriteriaEnum sortCriteria, 
                              String sortField, Boolean ascendingOrder) {
        this.elementsToSort = elementsToSort;
        this.sortCriteria = sortCriteria;
        this.sortField = sortField;
        this.ascendingOrder = ascendingOrder;
    }
    
    // Constructor completo
    public QuickSortRequestDTO(List<T> elementsToSort, OptimizationCriteriaEnum sortCriteria, 
                              String sortField, Boolean ascendingOrder, Integer limit) {
        this.elementsToSort = elementsToSort;
        this.sortCriteria = sortCriteria;
        this.sortField = sortField;
        this.ascendingOrder = ascendingOrder;
        this.limit = limit;
    }
    
    // Getters y Setters
    public List<T> getElementsToSort() {
        return elementsToSort;
    }
    
    public void setElementsToSort(List<T> elementsToSort) {
        this.elementsToSort = elementsToSort;
    }
    
    public OptimizationCriteriaEnum getSortCriteria() {
        return sortCriteria;
    }
    
    public void setSortCriteria(OptimizationCriteriaEnum sortCriteria) {
        this.sortCriteria = sortCriteria;
    }
    
    public String getSortField() {
        return sortField;
    }
    
    public void setSortField(String sortField) {
        this.sortField = sortField;
    }
    
    public Boolean getAscendingOrder() {
        return ascendingOrder;
    }
    
    public void setAscendingOrder(Boolean ascendingOrder) {
        this.ascendingOrder = ascendingOrder;
    }
    
    public Integer getLimit() {
        return limit;
    }
    
    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
