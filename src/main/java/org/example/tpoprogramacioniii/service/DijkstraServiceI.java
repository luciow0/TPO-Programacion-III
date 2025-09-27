package org.example.tpoprogramacioniii.service;

import org.example.tpoprogramacioniii.dto.request.RouteRequestDTO;
import org.example.tpoprogramacioniii.dto.response.RouteResponseDTO;

// Implementacion en el sistema:
// Cálculo de la ruta más corta/rápida/barata
// entre dos ubicaciones (origen y destino).

// Uso y justificacion:
// Algoritmo fundamental para la navegación y la planificación de rutas

public interface DijkstraServiceI {

    /**
     * Calcula la ruta óptima entre dos ubicaciones usando el algoritmo de Dijkstra
     *
     * @param request DTO que contiene el origen, destino y criterio de optimización
     * @return RouteResponseDTO con la ruta calculada, distancia, tiempo y algoritmo usado
     */
    RouteResponseDTO calculateOptimalRoute(RouteRequestDTO request);

    /**
     * Calcula la ruta más corta en distancia entre dos ubicaciones
     *
     * @param originLocationId ID de la ubicación de origen
     * @param destinationLocationId ID de la ubicación de destino
     * @return RouteResponseDTO con la ruta de menor distancia
     */
    RouteResponseDTO calculateShortestDistanceRoute(String originLocationId, String destinationLocationId);

    /**
     * Calcula la ruta más rápida en tiempo entre dos ubicaciones
     *
     * @param originLocationId ID de la ubicación de origen
     * @param destinationLocationId ID de la ubicación de destino
     * @return RouteResponseDTO con la ruta de menor tiempo
     */
    RouteResponseDTO calculateFastestTimeRoute(String originLocationId, String destinationLocationId);

    /**
     * Calcula la ruta más económica en costo entre dos ubicaciones
     *
     * @param originLocationId ID de la ubicación de origen
     * @param destinationLocationId ID de la ubicación de destino
     * @return RouteResponseDTO con la ruta de menor costo
     */
    RouteResponseDTO calculateCheapestCostRoute(String originLocationId, String destinationLocationId);

    /**
     * Calcula las rutas a todos los destinos posibles desde un origen dado
     *
     * @param originLocationId ID de la ubicación de origen
     * @return Lista de RouteResponseDTO con todas las rutas posibles
     */
    java.util.List<RouteResponseDTO> calculateAllRoutesFromOrigin(String originLocationId);

    /**
     * Verifica si existe una ruta válida entre dos ubicaciones
     *
     * @param originLocationId ID de la ubicación de origen
     * @param destinationLocationId ID de la ubicación de destino
     * @return true si existe una ruta, false en caso contrario
     */
    boolean hasValidRoute(String originLocationId, String destinationLocationId);

    /**
     * Obtiene información sobre el rendimiento del algoritmo
     *
     * @param request DTO de la última consulta realizada
     * @return Información sobre tiempo de ejecución, nodos visitados, etc.
     */
    java.util.Map<String, Object> getAlgorithmPerformanceInfo(RouteRequestDTO request);
}
