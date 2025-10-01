package org.example.tpoprogramacioniii.service;

import java.util.List;

public interface QuickSortServiceI {

    /**
     * Ordena las tareas por un parámetro del Task:
     *  - "priority" (default), "start"/"time_window_start", "end"/"time_window_end"
     * @param taskIds IDs de Task a ordenar (se respeta el universo dado)
     * @param param   campo por el que se ordena
     * @param asc     true ascendente, false descendente
     * @return lista de IDs ordenada
     */
    List<String> sortTasksByParam(List<String> taskIds, String param, boolean asc);
}
