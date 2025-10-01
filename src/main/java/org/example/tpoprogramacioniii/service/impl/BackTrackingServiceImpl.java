package org.example.tpoprogramacioniii.service.impl;

import org.example.tpoprogramacioniii.model.Task;
import org.example.tpoprogramacioniii.repository.TaskRepository;
import org.example.tpoprogramacioniii.service.BackTrackingServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;

@Service
public class BackTrackingServiceImpl implements BackTrackingServiceI {

    private final TaskRepository taskRepository;

    @Autowired
    public BackTrackingServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<String> selectTasksMaxPriorityNonOverlapping() {
        List<Task> all = taskRepository.findAll();

        all.sort(Comparator
                .comparing((Task t) -> Optional.ofNullable(t.getTime_window_end()).orElse(LocalTime.MAX))
                .thenComparing((Task t) -> Optional.ofNullable(t.getTime_window_start()).orElse(LocalTime.MIN))
                .thenComparing((Task t) -> -Optional.ofNullable(t.getPriority()).orElse(0)));

        Best best = new Best();
        backtrack(all, 0, new ArrayList<>(), 0, LocalTime.MIN, best);
        return best.ids;
    }

    private void backtrack(List<Task> tasks, int idx, List<String> chosen, int score, LocalTime lastEnd, Best best) {
        if (idx == tasks.size()) {
            if (score > best.score) {
                best.score = score;
                best.ids = new ArrayList<>(chosen);
            }
            return;
        }

        Task cur = tasks.get(idx);
        LocalTime s = Optional.ofNullable(cur.getTime_window_start()).orElse(LocalTime.MIN);
        LocalTime e = Optional.ofNullable(cur.getTime_window_end()).orElse(LocalTime.MAX);

        // Tomar
        if (!s.isBefore(lastEnd)) {
            chosen.add(cur.getId());
            backtrack(tasks, idx + 1, chosen, score + (cur.getPriority() != null ? cur.getPriority() : 0), e, best);
            chosen.remove(chosen.size() - 1);
        }

        // Saltar
        backtrack(tasks, idx + 1, chosen, score, lastEnd, best);
    }

    private static class Best {
        int score = Integer.MIN_VALUE;
        List<String> ids = new ArrayList<>();
    }
}
