package org.example.tpoprogramacioniii.service.impl;

import org.example.tpoprogramacioniii.model.Task;
import org.example.tpoprogramacioniii.service.BranchAndBoundServiceI;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

@Service
public class BranchAndBoundServiceImpl implements BranchAndBoundServiceI {

    public static class Node {
        int level;                 // Nivel en el árbol (índice de la tarea actual)
        List<Task> chosen;         // Tareas elegidas hasta el momento
        int count;                 // Cantidad de tareas elegidas
        double bound;              // Cota superior estimada (optimista)
        long lastEnd;               // Fin del último intervalo elegido
    }

    /**
     * Selecciona el mayor conjunto de tareas no solapadas usando Branch & Bound.
     *
     * @param tasks lista de tareas con (start, end)
     * @return lista máxima de tareas compatibles
     */
    public List<Task> intervalSchedulingBranchAndBound(List<Task> tasks) {
        // Ordenamos por tiempo de finalización (clásico en interval scheduling)
            tasks.sort(Comparator.comparingInt(t -> Math.toIntExact(t.getTime_window_end().toNanoOfDay())));

        PriorityQueue<Node> pq = new PriorityQueue<>((a, b) -> Double.compare(b.bound, a.bound));

        Node root = new Node();
        root.level = -1;
        root.count = 0;
        root.chosen = new ArrayList<>();
        root.lastEnd = Integer.MIN_VALUE;
        root.bound = calculateBound(root, tasks);

        pq.add(root);

        int maxCount = 0;
        List<Task> bestSolution = new ArrayList<>();

        while (!pq.isEmpty()) {
            Node u = pq.poll();

            if (u.bound > maxCount && u.level < tasks.size() - 1) {
                int nextLevel = u.level + 1;
                Task nextTask = tasks.get(nextLevel);

                // Caso 1: incluir la tarea si no se solapa
                if (nextTask.getTime_window_start().toNanoOfDay() >= u.lastEnd) {
                    Node v = cloneNode(u);
                    v.level = nextLevel;
                    v.chosen.add(nextTask);
                    v.count = u.count + 1;
                    v.lastEnd = nextTask.getTime_window_end().toNanoOfDay();
                    v.bound = calculateBound(v, tasks);

                    if (v.count > maxCount) {
                        maxCount = v.count;
                        bestSolution = new ArrayList<>(v.chosen);
                    }
                    if (v.bound > maxCount) pq.add(v);
                }

                // Caso 2: excluir la tarea
                Node w = cloneNode(u);
                w.level = nextLevel;
                w.bound = calculateBound(w, tasks);
                if (w.bound > maxCount) pq.add(w);
            }
        }

        return bestSolution;
    }

    /**
     * Estima una cota superior (bound): cantidad máxima de tareas que todavía podrían agregarse.
     */
    private double calculateBound(Node node, List<Task> tasks) {
        int result = node.count;
        int j = node.level + 1;
        long lastEnd = node.lastEnd;

        // Simulación greedy: agregar todas las tareas posibles sin solapamiento
        while (j < tasks.size()) {
            Task t = tasks.get(j);
            if (t.getTime_window_start().toNanoOfDay() >= lastEnd) {
                result++;
                lastEnd = t.getTime_window_end().toNanoOfDay();
            }
            j++;
        }
        return result;
    }

    private Node cloneNode(Node n) {
        Node copy = new Node();
        copy.level = n.level;
        copy.count = n.count;
        copy.bound = n.bound;
        copy.lastEnd = n.lastEnd;
        copy.chosen = new ArrayList<>(n.chosen);
        return copy;
    }
}
