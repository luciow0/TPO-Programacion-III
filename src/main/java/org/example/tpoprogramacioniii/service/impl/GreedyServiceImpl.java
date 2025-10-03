package org.example.tpoprogramacioniii.service.impl;

import org.example.tpoprogramacioniii.model.Task;
import org.example.tpoprogramacioniii.service.GreedyServiceI;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class GreedyServiceImpl implements GreedyServiceI {

    /**
     * Implementación del algoritmo Greedy para el problema de la Selección de Intervalos.
     * El objetivo es seleccionar el máximo número de tareas (intervalos) que no se solapen.
     *
     * Heurística: Seleccionar la tarea que tiene la hora de finalización más temprana
     * y, de forma iterativa, elegir la siguiente tarea compatible que comience después
     * de que la tarea previamente seleccionada haya finalizado.
     *
     * @param tasks La lista de todas las tareas (intervalos) disponibles.
     * @return La lista de tareas seleccionadas.
     */
    @Override
    public List<Task> selectTasksMaxNumberNonOverlapping(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return new ArrayList<>();
        }

        // 1. Crear una copia mutable de la lista para evitar modificar la original.
        List<Task> taskList = new ArrayList<>(tasks);

        // 2. Aplicar la heurística: ORDENAR por la HORA DE FINALIZACIÓN más temprana.
        // Tareas con ventana de tiempo inválida (start > end) o nula se ignorarán implícitamente
        // al no ser elegidas como la próxima actividad.
        taskList.sort(Comparator.comparing(Task::getTime_window_end));

        List<Task> selectedTasks = new ArrayList<>();

        // 3. Seleccionar la primera tarea (la que finaliza más temprano)
        Task firstTask = taskList.get(0);
        selectedTasks.add(firstTask);

        // Almacenar el tiempo de finalización de la última tarea seleccionada
        // para la verificación de no solapamiento.
        // Se asume que no hay "tiempo de viaje" entre la última tarea y la siguiente,
        // solo se considera el solapamiento de la ventana de tiempo.
        java.time.LocalTime lastFinishTime = firstTask.getTime_window_end();

        // 4. Iterar sobre las tareas restantes y seleccionar la primera compatible.
        // Empezamos desde la segunda tarea, ya que la primera ya fue seleccionada.
        for (int i = 1; i < taskList.size(); i++) {
            Task currentTask = taskList.get(i);

            // Una tarea es compatible si su hora de inicio es igual o posterior
            // a la hora de finalización de la última tarea seleccionada.
            if (currentTask.getTime_window_start() != null &&
                    lastFinishTime != null &&
                    !currentTask.getTime_window_start().isBefore(lastFinishTime)) {

                selectedTasks.add(currentTask);
                // Actualizar la hora de finalización para la siguiente comparación.
                lastFinishTime = currentTask.getTime_window_end();
            }
        }

        return selectedTasks;
    }
}