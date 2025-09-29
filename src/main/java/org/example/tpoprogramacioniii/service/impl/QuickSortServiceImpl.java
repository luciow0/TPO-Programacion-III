package org.example.tpoprogramacioniii.service.impl;

import org.example.tpoprogramacioniii.Enum.AlgorithmEnum;
import org.example.tpoprogramacioniii.dto.request.QuickSortRequestDTO;
import org.example.tpoprogramacioniii.dto.response.QuickSortResponseDTO;
import org.example.tpoprogramacioniii.model.Segment;
import org.example.tpoprogramacioniii.model.Task;
import org.example.tpoprogramacioniii.repository.SegmentRepository;
import org.example.tpoprogramacioniii.repository.TaskRepository;
import org.example.tpoprogramacioniii.service.QuickSortServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;

/**
 * Implementación de QuickSort con contadores de comparaciones e intercambios.
 * Usa repos de Neo4j para ordenar entidades reales.
 */
@Service
public class QuickSortServiceImpl implements QuickSortServiceI {

    private int lastComparisons = 0;
    private int lastSwaps = 0;
    private long lastExecutionTime = 0;

    private final TaskRepository taskRepository;
    private final SegmentRepository segmentRepository;

    @Autowired
    public QuickSortServiceImpl(TaskRepository taskRepository,
                                SegmentRepository segmentRepository) {
        this.taskRepository = taskRepository;
        this.segmentRepository = segmentRepository;
    }

    /* =================== API genérica =================== */

    @Override
    public <T extends Comparable<T>> QuickSortResponseDTO<T> sortElements(QuickSortRequestDTO<T> request) {
        long start = System.currentTimeMillis();
        lastComparisons = lastSwaps = 0;

        List<T> elements = request.getElementsToSort() == null
                ? new ArrayList<>()
                : new ArrayList<>(request.getElementsToSort());

        boolean ascending = request.getAscendingOrder() == null || request.getAscendingOrder();

        if (elements.size() > 1) {
            quickSort(elements, 0, elements.size() - 1, ascending);
        }

        if (request.getLimit() != null && request.getLimit() > 0 && request.getLimit() < elements.size()) {
            elements = new ArrayList<>(elements.subList(0, request.getLimit()));
        }

        lastExecutionTime = System.currentTimeMillis() - start;

        return new QuickSortResponseDTO<>(
                elements,
                AlgorithmEnum.QUICK_SORT,
                lastExecutionTime,
                request.getElementsToSort() == null ? 0 : request.getElementsToSort().size(),
                lastComparisons,
                lastSwaps,
                request.getSortCriteria() != null ? request.getSortCriteria().name() : "DEFAULT",
                ascending
        );
    }

    /* =================== Variantes de dominio =================== */

    @Override
    public QuickSortResponseDTO<String> sortTasksByPriority(List<String> taskIds, boolean ascendingOrder) {
        long start = System.currentTimeMillis();
        lastComparisons = lastSwaps = 0;

        List<Task> tasks = taskIds == null || taskIds.isEmpty()
                ? Collections.emptyList()
                : iterableToList(taskRepository.findAllById(taskIds));

        // Par (id, priority)
        List<TaskPriority> list = new ArrayList<>();
        for (Task t : tasks) {
            list.add(new TaskPriority(t.getId(), nz(t.getPriority())));
        }

        if (list.size() > 1) quickSort(list, 0, list.size() - 1, ascendingOrder);

        List<String> sortedIds = new ArrayList<>(list.size());
        for (TaskPriority p : list) sortedIds.add(p.taskId);

        lastExecutionTime = System.currentTimeMillis() - start;

        return new QuickSortResponseDTO<>(
                sortedIds, AlgorithmEnum.QUICK_SORT, lastExecutionTime,
                sortedIds.size(), lastComparisons, lastSwaps, "TASK_PRIORITY", ascendingOrder
        );
    }

    @Override
    public QuickSortResponseDTO<String> sortTasksByCreationTime(List<String> taskIds, boolean ascendingOrder) {
        long start = System.currentTimeMillis();
        lastComparisons = lastSwaps = 0;

        List<Task> tasks = taskIds == null || taskIds.isEmpty()
                ? Collections.emptyList()
                : iterableToList(taskRepository.findAllById(taskIds));

        // Interpretamos "creation time" como time_window_start
        List<TaskStartWindow> list = new ArrayList<>();
        for (Task t : tasks) {
            list.add(new TaskStartWindow(t.getId(), safe(t.getTime_window_start())));
        }

        if (list.size() > 1) quickSort(list, 0, list.size() - 1, ascendingOrder);

        List<String> sortedIds = new ArrayList<>(list.size());
        for (TaskStartWindow tw : list) sortedIds.add(tw.taskId);

        lastExecutionTime = System.currentTimeMillis() - start;

        return new QuickSortResponseDTO<>(
                sortedIds, AlgorithmEnum.QUICK_SORT, lastExecutionTime,
                sortedIds.size(), lastComparisons, lastSwaps, "TASK_TIME_WINDOW_START", ascendingOrder
        );
    }

    @Override
    public QuickSortResponseDTO<String> sortSegmentsByDistance(List<String> segmentIds, boolean ascendingOrder) {
        long start = System.currentTimeMillis();
        lastComparisons = lastSwaps = 0;

        List<Segment> segs = segmentIds == null || segmentIds.isEmpty()
                ? Collections.emptyList()
                : iterableToList(segmentRepository.findAllById(segmentIds));

        List<SegmentDistance> list = new ArrayList<>();
        for (Segment s : segs) {
            list.add(new SegmentDistance(s.getId(), nz(s.getDistanceKm())));
        }

        if (list.size() > 1) quickSort(list, 0, list.size() - 1, ascendingOrder);

        List<String> sortedIds = new ArrayList<>(list.size());
        for (SegmentDistance sd : list) sortedIds.add(sd.segmentId);

        lastExecutionTime = System.currentTimeMillis() - start;

        return new QuickSortResponseDTO<>(
                sortedIds, AlgorithmEnum.QUICK_SORT, lastExecutionTime,
                sortedIds.size(), lastComparisons, lastSwaps, "SEGMENT_DISTANCE_KM", ascendingOrder
        );
    }

    @Override
    public QuickSortResponseDTO<String> sortSegmentsByTravelTime(List<String> segmentIds, boolean ascendingOrder) {
        long start = System.currentTimeMillis();
        lastComparisons = lastSwaps = 0;

        List<Segment> segs = segmentIds == null || segmentIds.isEmpty()
                ? Collections.emptyList()
                : iterableToList(segmentRepository.findAllById(segmentIds));

        List<SegmentTime> list = new ArrayList<>();
        for (Segment s : segs) {
            list.add(new SegmentTime(s.getId(), nz(s.getTimeMin())));
        }

        if (list.size() > 1) quickSort(list, 0, list.size() - 1, ascendingOrder);

        List<String> sortedIds = new ArrayList<>(list.size());
        for (SegmentTime st : list) sortedIds.add(st.segmentId);

        lastExecutionTime = System.currentTimeMillis() - start;

        return new QuickSortResponseDTO<>(
                sortedIds, AlgorithmEnum.QUICK_SORT, lastExecutionTime,
                sortedIds.size(), lastComparisons, lastSwaps, "SEGMENT_TIME_MIN", ascendingOrder
        );
    }

    /* =================== utilidades / métricas =================== */

    @Override
    public Map<String, Object> getLastSortingStatistics() {
        Map<String, Object> m = new HashMap<>();
        m.put("comparisons", lastComparisons);
        m.put("swaps", lastSwaps);
        m.put("executionTimeMs", lastExecutionTime);
        m.put("algorithm", "QUICK_SORT");
        return m;
    }

    @Override
    public <T extends Comparable<T>> boolean isSorted(List<T> elements, boolean ascendingOrder) {
        if (elements == null || elements.size() <= 1) return true;
        for (int i = 0; i < elements.size() - 1; i++) {
            int c = elements.get(i).compareTo(elements.get(i + 1));
            if (ascendingOrder && c > 0) return false;
            if (!ascendingOrder && c < 0) return false;
        }
        return true;
    }

    /* =================== QuickSort genérico (in-place) =================== */

    private <T extends Comparable<T>> void quickSort(List<T> arr, int left, int right, boolean asc) {
        if (left >= right) return;
        int p = partition(arr, left, right, asc);
        quickSort(arr, left, p - 1, asc);
        quickSort(arr, p + 1, right, asc);
    }

    private <T extends Comparable<T>> int partition(List<T> arr, int left, int right, boolean asc) {
        T pivot = arr.get(right);
        int i = left;
        for (int j = left; j < right; j++) {
            lastComparisons++;
            int cmp = arr.get(j).compareTo(pivot);
            if ((asc && cmp <= 0) || (!asc && cmp >= 0)) {
                swap(arr, i, j);
                i++;
            }
        }
        swap(arr, i, right);
        return i;
    }

    private <T> void swap(List<T> arr, int i, int j) {
        if (i == j) return;
        lastSwaps++;
        T tmp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, tmp);
    }

    /* =================== helpers internos =================== */

    private static int nz(Integer v) { return v == null ? 0 : v; }
    private static double nz(Double v) { return v == null ? 0.0 : v; }
    private static LocalTime safe(LocalTime t) { return t == null ? LocalTime.MIN : t; }

    private static <T> List<T> iterableToList(Iterable<T> it) {
        List<T> out = new ArrayList<>();
        if (it != null) for (T x : it) out.add(x);
        return out;
    }

    /* =================== tipos comparables (pares id-valor) =================== */

    private static final class TaskPriority implements Comparable<TaskPriority> {
        final String taskId; final int priority;
        TaskPriority(String taskId, int priority) { this.taskId = taskId; this.priority = priority; }
        @Override public int compareTo(TaskPriority o) { return Integer.compare(this.priority, o.priority); }
    }

    private static final class TaskStartWindow implements Comparable<TaskStartWindow> {
        final String taskId; final LocalTime start;
        TaskStartWindow(String taskId, LocalTime start) { this.taskId = taskId; this.start = start; }
        @Override public int compareTo(TaskStartWindow o) { return this.start.compareTo(o.start); }
    }

    private static final class SegmentDistance implements Comparable<SegmentDistance> {
        final String segmentId; final double distance;
        SegmentDistance(String segmentId, double distance) { this.segmentId = segmentId; this.distance = distance; }
        @Override public int compareTo(SegmentDistance o) { return Double.compare(this.distance, o.distance); }
    }

    private static final class SegmentTime implements Comparable<SegmentTime> {
        final String segmentId; final double time;
        SegmentTime(String segmentId, double time) { this.segmentId = segmentId; this.time = time; }
        @Override public int compareTo(SegmentTime o) { return Double.compare(this.time, o.time); }
    }
}
