package org.example.tpoprogramacioniii.service;

import org.example.tpoprogramacioniii.dto.request.RouteRequestDTO;
import org.example.tpoprogramacioniii.dto.response.RouteResponseDTO;

import java.util.Map;

/**
 * Servicio para cálculo de rutas con Dijkstra.
 * Provee un método genérico (por DTO) y atajos por criterio.
 */
public interface DijkstraServiceI {

    /** Camino mínimo por DISTANCIA entre dos ubicaciones. */
    RouteResponseDTO calculateShortestDistanceRoute(String originLocationId, String destinationLocationId);

    /** Camino mínimo por TIEMPO entre dos ubicaciones. */
    RouteResponseDTO calculateFastestTimeRoute(String originLocationId, String destinationLocationId);

    /** Camino mínimo por COSTO (combustible) entre dos ubicaciones. */
    RouteResponseDTO calculateCheapestCostRoute(String originLocationId, String destinationLocationId);

    /** Método genérico que permite elegir criterio vía DTO. */
    RouteResponseDTO calculateOptimalRoute(RouteRequestDTO request);

    /** Verifica si existe ruta válida entre dos ubicaciones. */
    boolean hasValidRoute(String originLocationId, String destinationLocationId);

    /** Métricas de la última corrida (tiempo, nodos visitados, etc.). */
    Map<String, Object> getAlgorithmPerformanceInfo(RouteRequestDTO request);
}
