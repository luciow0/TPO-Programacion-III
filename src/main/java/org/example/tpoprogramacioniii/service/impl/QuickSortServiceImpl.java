package org.example.tpoprogramacioniii.service.impl;

import org.example.tpoprogramacioniii.model.Location;
import org.example.tpoprogramacioniii.model.Task;
import org.example.tpoprogramacioniii.repository.TaskRepository;
import org.example.tpoprogramacioniii.service.QuickSortServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class QuickSortServiceImpl implements QuickSortServiceI {

    private final TaskRepository taskRepository;

    @Autowired
    public QuickSortServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<String> sortTasksByParam(List<String> taskIds, String param, boolean asc) {
        if (taskIds == null || taskIds.isEmpty()) return List.of();

        // Map<String, Task> tipado
        Map<String, Task> map = taskRepository.findAllById(taskIds).stream()
                .filter(t -> t.getId() != null)
                .collect(Collectors.toMap(Task::getId, Function.identity()));

        // List<Task> tipada (no raw)
        List<Task> tasks = new ArrayList<>();
        for (String id : taskIds) {
            Task t = map.get(id);
            if (t != null) tasks.add(t); // respeta el universo de IDs pedido
        }

        // Comparator<Task> tipado
        Comparator<Task> cmp = buildComparator(param);
        if (!asc) cmp = cmp.reversed();

        quickSort(tasks, 0, tasks.size() - 1, cmp);

        // Devolvemos IDs
        List<String> result = new ArrayList<>(tasks.size());
        for (Task t : tasks) result.add(t.getId());
        return result;
    }

    private Comparator<Task> buildComparator(String param) {
        String p = (param == null ? "priority" : param.trim().toLowerCase(Locale.ROOT));
        switch (p) {
            case "start":
            case "time_window_start":
                return Comparator.comparing(
                        (Task t) -> Optional.ofNullable(t.getTime_window_start()).orElse(LocalTime.MIN)
                ).thenComparing(t -> Optional.ofNullable(t.getId()).orElse(""));
            case "end":
            case "time_window_end":
                return Comparator.comparing(
                        (Task t) -> Optional.ofNullable(t.getTime_window_end()).orElse(LocalTime.MAX)
                ).thenComparing(t -> Optional.ofNullable(t.getId()).orElse(""));
            case "destino":
            case "destination":
                return Comparator.comparing(
                        (Task t) -> Optional.ofNullable(t.getDestino())
                                .map(Location::getName)
                                .orElse(""),
                        String.CASE_INSENSITIVE_ORDER
                ).thenComparing(t -> Optional.ofNullable(t.getId()).orElse(""));
            case "priority":
            default:
                return Comparator.comparingInt(
                        (Task t) -> Optional.ofNullable(t.getPriority()).orElse(0)
                ).thenComparing(t -> Optional.ofNullable(t.getId()).orElse(""));
        }
    }

    /* ================= QuickSort in-place ================= */

    private void quickSort(List<Task> a, int lo, int hi, Comparator<Task> cmp) {
        if (lo >= hi) return;
        int p = partition(a, lo, hi, cmp);
        quickSort(a, lo, p - 1, cmp);
        quickSort(a, p + 1, hi, cmp);
    }

    private int partition(List<Task> a, int lo, int hi, Comparator<Task> cmp) {
        Task pivot = a.get(hi);
        int i = lo;
        for (int j = lo; j < hi; j++) {
            if (cmp.compare(a.get(j), pivot) <= 0) {
                Collections.swap(a, i, j);
                i++;
            }
        }
        Collections.swap(a, i, hi);
        return i;
    }
}
