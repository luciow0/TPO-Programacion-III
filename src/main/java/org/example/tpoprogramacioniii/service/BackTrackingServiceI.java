package org.example.tpoprogramacioniii.service;

import java.util.List;

public interface BackTrackingServiceI {

    /**
     * Selecciona el subconjunto de tareas (Task) no solapadas por ventana de tiempo
     * que maximiza la suma de prioridad. Retorna los IDs de Task elegidos.
     */
    List<String> selectTasksMaxPriorityNonOverlapping();
}
