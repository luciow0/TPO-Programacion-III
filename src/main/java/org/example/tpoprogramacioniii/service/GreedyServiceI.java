package org.example.tpoprogramacioniii.service;

import org.example.tpoprogramacioniii.model.Task;

import java.util.List;

public interface GreedyServiceI {

    /**
     * Aplica el algoritmo Greedy para la selección de intervalos,
     * eligiendo un conjunto máximo de tareas no solapadas.
     * La heurística Greedy elegida será ordenar por la hora de finalización más temprana.
     *
     * @param tasks La lista de todas las tareas disponibles.
     * @return Una lista de objetos Task seleccionados que no se solapan.
     */
    List<Task> selectTasksMaxNumberNonOverlapping(List<Task> tasks);
}