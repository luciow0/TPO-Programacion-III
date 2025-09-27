package org.example.tpoprogramacioniii.service;

import org.example.tpoprogramacioniii.dto.request.BackTrackingRequestDTO;
import org.example.tpoprogramacioniii.dto.response.RouteResponseDTO;

import java.util.List;

// Implementacion en el sistema:
// Explorar combinaciones de rutas para
// encontrar una que cumpla con múltiples
// restricciones (ej. pasar por el nodo A, no pasar
// por la zona B, y la duración total es menor a X).

// Uso y justificacion:
// Búsqueda exhaustiva con poda temprana
// para problemas de satisfacción de restricciones.

public interface BackTrackingServiceI {

    /**
     * Encuentra una ruta que cumpla con todas las restricciones especificadas usando BackTracking
     *
     * @param request DTO que contiene origen, destino, restricciones y criterios de optimización
     * @return RouteResponseDTO con la primera ruta válida encontrada que cumple las restricciones
     */
    RouteResponseDTO findConstrainedRoute(BackTrackingRequestDTO request);

    /**
     * Encuentra todas las rutas posibles que cumplan con las restricciones especificadas
     *
     * @param request DTO que contiene origen, destino, restricciones y criterios de optimización
     * @param maxSolutions Número máximo de soluciones a retornar
     * @return Lista de RouteResponseDTO con todas las rutas válidas encontradas
     */
    List<RouteResponseDTO> findAllConstrainedRoutes(BackTrackingRequestDTO request, int maxSolutions);

    /**
     * Encuentra la mejor ruta que cumpla con las restricciones usando BackTracking
     *
     * @param request DTO que contiene origen, destino, restricciones y criterios de optimización
     * @return RouteResponseDTO con la mejor ruta encontrada según el criterio de optimización
     */
    RouteResponseDTO findOptimalConstrainedRoute(BackTrackingRequestDTO request);

    /**
     * Encuentra rutas que pasen obligatoriamente por ubicaciones específicas
     *
     * @param originLocationId ID de la ubicación de origen
     * @param destinationLocationId ID de la ubicación de destino
     * @param mandatoryLocationIds Lista de IDs de ubicaciones que deben ser visitadas
     * @param optimizationCriteria Criterio de optimización (TIME, DISTANCE, COST)
     * @return RouteResponseDTO con la ruta que pasa por todas las ubicaciones obligatorias
     */
    RouteResponseDTO findRouteWithMandatoryLocations(String originLocationId,
                                                     String destinationLocationId,
                                                     List<String> mandatoryLocationIds,
                                                     String optimizationCriteria);

    /**
     * Encuentra rutas evitando ubicaciones específicas
     *
     * @param originLocationId ID de la ubicación de origen
     * @param destinationLocationId ID de la ubicación de destino
     * @param forbiddenLocationIds Lista de IDs de ubicaciones que NO deben ser visitadas
     * @param optimizationCriteria Criterio de optimización (TIME, DISTANCE, COST)
     * @return RouteResponseDTO con la ruta que evita todas las ubicaciones prohibidas
     */
    RouteResponseDTO findRouteAvoidingLocations(String originLocationId,
                                                String destinationLocationId,
                                                List<String> forbiddenLocationIds,
                                                String optimizationCriteria);

    /**
     * Encuentra rutas con restricciones de tiempo máximo
     *
     * @param originLocationId ID de la ubicación de origen
     * @param destinationLocationId ID de la ubicación de destino
     * @param maxTimeMinutes Tiempo máximo permitido en minutos
     * @param optimizationCriteria Criterio de optimización (TIME, DISTANCE, COST)
     * @return RouteResponseDTO con la ruta que cumple la restricción de tiempo
     */
    RouteResponseDTO findRouteWithTimeConstraint(String originLocationId,
                                                 String destinationLocationId,
                                                 double maxTimeMinutes,
                                                 String optimizationCriteria);

    /**
     * Encuentra rutas con restricciones de distancia máxima
     *
     * @param originLocationId ID de la ubicación de origen
     * @param destinationLocationId ID de la ubicación de destino
     * @param maxDistanceKm Distancia máxima permitida en kilómetros
     * @param optimizationCriteria Criterio de optimización (TIME, DISTANCE, COST)
     * @return RouteResponseDTO con la ruta que cumple la restricción de distancia
     */
    RouteResponseDTO findRouteWithDistanceConstraint(String originLocationId,
                                                     String destinationLocationId,
                                                     double maxDistanceKm,
                                                     String optimizationCriteria);

    /**
     * Encuentra rutas con un número máximo de paradas
     *
     * @param originLocationId ID de la ubicación de origen
     * @param destinationLocationId ID de la ubicación de destino
     * @param maxStops Número máximo de paradas/intermedias permitidas
     * @param optimizationCriteria Criterio de optimización (TIME, DISTANCE, COST)
     * @return RouteResponseDTO con la ruta que cumple la restricción de paradas
     */
    RouteResponseDTO findRouteWithMaxStops(String originLocationId,
                                           String destinationLocationId,
                                           int maxStops,
                                           String optimizationCriteria);

    /**
     * Verifica si es posible encontrar una ruta que cumpla con todas las restricciones
     *
     * @param request DTO que contiene origen, destino y restricciones
     * @return true si existe al menos una ruta válida, false en caso contrario
     */
    boolean hasValidConstrainedRoute(BackTrackingRequestDTO request);

    /**
     * Obtiene estadísticas del proceso de búsqueda con BackTracking
     *
     * @param request DTO de la última consulta realizada
     * @return Map con información sobre nodos explorados, podas realizadas, tiempo de ejecución, etc.
     */
    java.util.Map<String, Object> getBackTrackingStatistics(BackTrackingRequestDTO request);

    /**
     * Encuentra rutas con restricciones múltiples combinadas
     *
     * @param request DTO que contiene todas las restricciones a aplicar
     * @param findBestSolution true para encontrar la mejor solución, false para la primera válida
     * @return RouteResponseDTO con la ruta que cumple todas las restricciones
     */
    RouteResponseDTO findMultiConstrainedRoute(BackTrackingRequestDTO request, boolean findBestSolution);
}
