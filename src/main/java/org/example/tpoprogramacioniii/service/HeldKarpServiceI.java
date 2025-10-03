package org.example.tpoprogramacioniii.service;

import org.example.tpoprogramacioniii.Enum.OptimizationCriteriaEnum;
import org.example.tpoprogramacioniii.model.Task;

import java.util.List;
import java.util.Map;

// TSP
// PD
public interface HeldKarpServiceI {

    /**
     * Calcula la ruta óptima para visitar todas las ubicaciones de las tareas dadas,
     * minimizando el costo total según el criterio de optimización.
     *
     * @param tasks Lista de tareas cuyas ubicaciones deben ser visitadas.
     * @param startLocationId El ID de la ubicación de inicio (ej. la base o el destino de la primera tarea).
     * @param criteria El criterio para medir el costo (DISTANCE_KM, TIME_MIN, COST_FUEL).
     * @return Un mapa que contiene la ruta óptima ('path') como una lista de IDs de Location y el costo total ('totalCost').
     */
    Map<String, Object> findOptimalTspRoute(List<Task> tasks, String startLocationId, OptimizationCriteriaEnum criteria);
}