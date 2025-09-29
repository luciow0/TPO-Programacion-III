package org.example.tpoprogramacioniii.service.impl;

import org.example.tpoprogramacioniii.model.Task;
import org.example.tpoprogramacioniii.repository.TaskRepository;
import org.example.tpoprogramacioniii.service.BackTrackingServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;

/**
 * Backtracking puro para seleccionar el mejor subconjunto de Task maximizando priority
 * respetando ventanas [time_window_start, time_window_end].
 * Supuesto: ejecución "instantánea" dentro de la ventana (sin duración de servicio).
 *
 * Heurística: ordena por fin de ventana asc, inicio asc, y prioridad desc para mejorar la exploración.
 */
@Service
public class BackTrackingServiceImpl implements BackTrackingServiceI {

    private final TaskRepository taskRepository;

    // Métricas de la última ejecución
    private volatile int lastExploredNodes = 0;
    private volatile long lastExecutionTimeMs = 0L;
    private volatile int lastBestPriority = 0;
    private volatile LocalTime lastFinishTime = LocalTime.MIN;
    private volatile int lastSelectedCount = 0;

    @Autowired
    public BackTrackingServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /* =================== API =================== */

    @Override
    public TaskSelectionResult selectBestTasksFromDb(LocalTime dayStart) {
        long t0 = System.currentTimeMillis();
        resetMetrics();

        List<Task> tasks = iterableToList(taskRepository.findAll());
        TaskSelectionResult res = doSelect(tasks, dayStart);

        updateMetrics(res, t0);
        return res;
    }

    @Override
    public TaskSelectionResult selectBestTasksByIds(List<String> taskIds, LocalTime dayStart) {
        long t0 = System.currentTimeMillis();
        resetMetrics();

        if (taskIds == null || taskIds.isEmpty()) {
            TaskSelectionResult empty = new TaskSelectionResult(Collections.emptyList(), 0, dayStart);
            updateMetrics(empty, t0);
            return empty;
        }

        List<Task> tasks = iterableToList(taskRepository.findAllById(taskIds));
        TaskSelectionResult res = doSelect(tasks, dayStart);

        updateMetrics(res, t0);
        return res;
    }

    @Override
    public TaskSelectionResult selectBestTasks(List<Task> tasks, LocalTime dayStart) {
        long t0 = System.currentTimeMillis();
        resetMetrics();

        TaskSelectionResult res = doSelect(tasks, dayStart);

        updateMetrics(res, t0);
        return res;
    }

    @Override
    public Map<String, Object> getLastSelectionStatistics() {
        Map<String, Object> m = new HashMap<>();
        m.put("algorithm", "BACKTRACKING");
        m.put("exploredNodes", lastExploredNodes);
        m.put("executionTimeMs", lastExecutionTimeMs);
        m.put("bestPriority", lastBestPriority);
        m.put("finishTime", String.valueOf(lastFinishTime));
        m.put("selectedCount", lastSelectedCount);
        return m;
    }

    /* =================== Núcleo Backtracking =================== */

    private TaskSelectionResult doSelect(List<Task> tasks, LocalTime dayStart) {
        Objects.requireNonNull(dayStart, "dayStart");
        List<Task> input = tasks == null ? Collections.emptyList() : new ArrayList<>(tasks);

        // Orden heurístico: end ↑, start ↑, priority ↓ (mejora soluciones tempranas y poda por factibilidad)
        input.sort(Comparator
                .comparing(this::safeEnd)
                .thenComparing(this::safeStart)
                .thenComparing((Task t) -> -nz(t.getPriority()))
                .thenComparing(Task::getId, Comparator.nullsLast(Comparator.naturalOrder())));

        Best best = new Best();
        backtrack(input, 0, dayStart, 0, new ArrayList<>(), best);
        return new TaskSelectionResult(best.bestSet, best.bestPriority, best.finishTime);
    }

    private static final class Best {
        int bestPriority = 0;
        LocalTime finishTime = LocalTime.MIN;
        List<Task> bestSet = new ArrayList<>();
    }

    private void backtrack(List<Task> tasks,
                           int idx,
                           LocalTime currentTime,
                           int accPriority,
                           List<Task> currentSet,
                           Best best) {
        lastExploredNodes++;

        // Actualizar mejor (desempate: terminar antes)
        if (accPriority > best.bestPriority ||
                (accPriority == best.bestPriority && currentTime.isBefore(best.finishTime))) {
            best.bestPriority = accPriority;
            best.finishTime = currentTime;
            best.bestSet = new ArrayList<>(currentSet);
        }

        if (idx == tasks.size()) return;

        Task t = tasks.get(idx);
        LocalTime start = safeStart(t);
        LocalTime end   = safeEnd(t);
        int value       = nz(t.getPriority());

        // Opción 1: incluir t si es factible en tiempo (hay algún t' en [max(currentTime, start), end])
        LocalTime feasibleStart = max(currentTime, start);
        if (!feasibleStart.isAfter(end)) {
            currentSet.add(t);
            backtrack(tasks, idx + 1, feasibleStart, accPriority + value, currentSet, best);
            currentSet.remove(currentSet.size() - 1);
        }

        // Opción 2: NO incluir t
        backtrack(tasks, idx + 1, currentTime, accPriority, currentSet, best);
    }

    /* =================== helpers =================== */

    private void resetMetrics() {
        lastExploredNodes = 0;
        lastExecutionTimeMs = 0L;
        lastBestPriority = 0;
        lastFinishTime = LocalTime.MIN;
        lastSelectedCount = 0;
    }

    private void updateMetrics(TaskSelectionResult res, long t0) {
        lastExecutionTimeMs = System.currentTimeMillis() - t0;
        lastBestPriority = res.getTotalPriority();
        lastFinishTime = res.getFinishTime();
        lastSelectedCount = res.getTasks().size();
    }

    private static <T> List<T> iterableToList(Iterable<T> it) {
        List<T> out = new ArrayList<>();
        if (it != null) for (T x : it) out.add(x);
        return out;
    }

    private LocalTime safeStart(Task t) {
        LocalTime s = t.getTime_window_start();
        return s == null ? LocalTime.MIN : s;
        // Si querés forzar s >= dayStart, movelo en doSelect según tu política.
    }

    private LocalTime safeEnd(Task t) {
        LocalTime e = t.getTime_window_end();
        return e == null ? LocalTime.MAX : e;
    }

    private static LocalTime max(LocalTime a, LocalTime b) {
        return a.isAfter(b) ? a : b;
    }

    private static int nz(Integer v) { return v == null ? 0 : v; }
}
