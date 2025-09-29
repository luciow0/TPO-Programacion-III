package org.example.tpoprogramacioniii.service;

import org.example.tpoprogramacioniii.model.Task;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
 * Servicio de selección de tareas por Backtracking.
 * - Maximiza la suma de priority.
 * - Respeta ventanas temporales [time_window_start, time_window_end].
 * - Supuesto: ejecución instantánea dentro de la ventana.
 */
public interface BackTrackingServiceI {

    /**
     * Selecciona el mejor subconjunto de tareas (max priority) a partir de TODAS
     * las tareas en Neo4j, comenzando desde dayStart.
     */
    TaskSelectionResult selectBestTasksFromDb(LocalTime dayStart);

    /**
     * Selecciona el mejor subconjunto de tareas (max priority) considerando SOLO
     * las tareas indicadas por sus IDs (si la lista es vacía o nula, se comporta como ningún resultado).
     */
    TaskSelectionResult selectBestTasksByIds(List<String> taskIds, LocalTime dayStart);

    /**
     * Variante in-memory (útil para tests o si ya tenés la lista cargada).
     */
    TaskSelectionResult selectBestTasks(List<Task> tasks, LocalTime dayStart);

    /**
     * Métricas de la última ejecución (exploredNodes, executionTimeMs, bestPriority, finishTime, count, algorithm).
     */
    Map<String, Object> getLastSelectionStatistics();

    /**
     * Resultado de selección (inmutable).
     */
    final class TaskSelectionResult {
        private final List<Task> tasks;
        private final int totalPriority;
        private final LocalTime finishTime;

        public TaskSelectionResult(List<Task> tasks, int totalPriority, LocalTime finishTime) {
            this.tasks = List.copyOf(tasks);
            this.totalPriority = totalPriority;
            this.finishTime = finishTime;
        }

        public List<Task> getTasks() { return tasks; }
        public int getTotalPriority() { return totalPriority; }
        public LocalTime getFinishTime() { return finishTime; }

        @Override
        public String toString() {
            return "TaskSelectionResult{count=%d, totalPriority=%d, finish=%s}"
                    .formatted(tasks.size(), totalPriority, finishTime);
        }
    }
}
