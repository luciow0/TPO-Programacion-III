package org.example.tpoprogramacioniii.service;

import org.example.tpoprogramacioniii.dto.request.QuickSortRequestDTO;
import org.example.tpoprogramacioniii.dto.response.QuickSortResponseDTO;

import java.util.List;

// Implementacion en el sistema:
// Ordenar los nodos o aristas por alguna métrica
// (distancia, prioridad, tráfico) antes de
// aplicar otro algoritmo o para presentar resultados.

// Uso y justificacion:
// Optimización de preprocesamiento, por ejemplo,
// ordenar los paquetes por prioridad de entrega.

public interface QuickSortServiceI {

    /**
     * Ordena una lista de elementos usando el algoritmo QuickSort
     *
     * @param request DTO que contiene los elementos a ordenar y criterios
     * @param <T> Tipo de elementos a ordenar (debe implementar Comparable)
     * @return QuickSortResponseDTO con los elementos ordenados y estadísticas
     */
    <T extends Comparable<T>> QuickSortResponseDTO<T> sortElements(QuickSortRequestDTO<T> request);

    /**
     * Ordena ubicaciones por distancia desde un punto de referencia
     *
     * @param locationIds Lista de IDs de ubicaciones a ordenar
     * @param referenceLatitude Latitud del punto de referencia
     * @param referenceLongitude Longitud del punto de referencia
     * @param ascendingOrder true para orden ascendente, false para descendente
     * @return QuickSortResponseDTO con las ubicaciones ordenadas por distancia
     */
    QuickSortResponseDTO<String> sortLocationsByDistance(List<String> locationIds,
                                                         double referenceLatitude,
                                                         double referenceLongitude,
                                                         boolean ascendingOrder);

    /**
     * Ordena tareas por prioridad
     *
     * @param taskIds Lista de IDs de tareas a ordenar
     * @param ascendingOrder true para orden ascendente, false para descendente
     * @return QuickSortResponseDTO con las tareas ordenadas por prioridad
     */
    QuickSortResponseDTO<String> sortTasksByPriority(List<String> taskIds, boolean ascendingOrder);

    /**
     * Ordena tareas por tiempo de creación
     *
     * @param taskIds Lista de IDs de tareas a ordenar
     * @param ascendingOrder true para orden ascendente, false para descendente
     * @return QuickSortResponseDTO con las tareas ordenadas por tiempo
     */
    QuickSortResponseDTO<String> sortTasksByCreationTime(List<String> taskIds, boolean ascendingOrder);

    /**
     * Ordena vehículos por capacidad
     *
     * @param vehicleIds Lista de IDs de vehículos a ordenar
     * @param ascendingOrder true para orden ascendente, false para descendente
     * @return QuickSortResponseDTO con los vehículos ordenados por capacidad
     */
    QuickSortResponseDTO<String> sortVehiclesByCapacity(List<String> vehicleIds, boolean ascendingOrder);

    /**
     * Ordena vehículos por consumo de combustible
     *
     * @param vehicleIds Lista de IDs de vehículos a ordenar
     * @param ascendingOrder true para orden ascendente, false para descendente
     * @return QuickSortResponseDTO con los vehículos ordenados por consumo
     */
    QuickSortResponseDTO<String> sortVehiclesByFuelConsumption(List<String> vehicleIds, boolean ascendingOrder);

    /**
     * Ordena segmentos de ruta por distancia
     *
     * @param segmentIds Lista de IDs de segmentos a ordenar
     * @param ascendingOrder true para orden ascendente, false para descendente
     * @return QuickSortResponseDTO con los segmentos ordenados por distancia
     */
    QuickSortResponseDTO<String> sortSegmentsByDistance(List<String> segmentIds, boolean ascendingOrder);

    /**
     * Ordena segmentos de ruta por tiempo de viaje
     *
     * @param segmentIds Lista de IDs de segmentos a ordenar
     * @param ascendingOrder true para orden ascendente, false para descendente
     * @return QuickSortResponseDTO con los segmentos ordenados por tiempo
     */
    QuickSortResponseDTO<String> sortSegmentsByTravelTime(List<String> segmentIds, boolean ascendingOrder);

    /**
     * Obtiene estadísticas del último proceso de ordenamiento
     *
     * @return Map con información sobre comparaciones, intercambios, tiempo de ejecución, etc.
     */
    java.util.Map<String, Object> getLastSortingStatistics();

    /**
     * Verifica si una lista está ordenada según el criterio especificado
     *
     * @param elements Lista de elementos a verificar
     * @param ascendingOrder true para verificar orden ascendente, false para descendente
     * @param <T> Tipo de elementos (debe implementar Comparable)
     * @return true si la lista está ordenada, false en caso contrario
     */
    <T extends Comparable<T>> boolean isSorted(List<T> elements, boolean ascendingOrder);
}
