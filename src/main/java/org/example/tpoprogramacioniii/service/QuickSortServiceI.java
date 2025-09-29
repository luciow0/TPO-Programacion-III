package org.example.tpoprogramacioniii.service;

import org.example.tpoprogramacioniii.dto.request.QuickSortRequestDTO;
import org.example.tpoprogramacioniii.dto.response.QuickSortResponseDTO;

import java.util.List;
import java.util.Map;

/**
 * Servicio de ordenamiento por QuickSort (in-place).
 * Incluye variantes para entidades del dominio (Task/Segment) y un método genérico.
 */
public interface QuickSortServiceI {

    /**
     * Ordena una lista genérica de elementos Comparable usando QuickSort.
     * @param request DTO con elementos y flags (ascending, limit, etc).
     */
    <T extends Comparable<T>> QuickSortResponseDTO<T> sortElements(QuickSortRequestDTO<T> request);

    /**
     * Ordena IDs de Task por prioridad (usa TaskRepository).
     * @param taskIds IDs de tasks a ordenar (si está vacío, no devuelve nada).
     * @param ascendingOrder true asc / false desc
     */
    QuickSortResponseDTO<String> sortTasksByPriority(List<String> taskIds, boolean ascendingOrder);

    /**
     * Ordena IDs de Task por ventana de inicio (interpreta “creation time” como time_window_start).
     * @param taskIds IDs de tasks
     * @param ascendingOrder true asc / false desc
     */
    QuickSortResponseDTO<String> sortTasksByCreationTime(List<String> taskIds, boolean ascendingOrder);

    /**
     * Ordena IDs de Segment por distanciaKm (usa SegmentRepository).
     * @param segmentIds IDs de segmentos
     * @param ascendingOrder true asc / false desc
     */
    QuickSortResponseDTO<String> sortSegmentsByDistance(List<String> segmentIds, boolean ascendingOrder);

    /**
     * Ordena IDs de Segment por timeMin (tiempo de viaje).
     * @param segmentIds IDs de segmentos
     * @param ascendingOrder true asc / false desc
     */
    QuickSortResponseDTO<String> sortSegmentsByTravelTime(List<String> segmentIds, boolean ascendingOrder);

    /**
     * Métricas de la última ejecución (comparisons, swaps, executionTimeMs, algorithm).
     */
    Map<String, Object> getLastSortingStatistics();

    /**
     * Chequea si una lista Comparable está ya ordenada (asc/desc).
     */
    <T extends Comparable<T>> boolean isSorted(List<T> elements, boolean ascendingOrder);
}
