package org.example.tpoprogramacioniii.View;

import org.example.tpoprogramacioniii.model.Location;
import org.example.tpoprogramacioniii.model.Task;
import org.example.tpoprogramacioniii.repository.LocationRepository;
import org.example.tpoprogramacioniii.repository.TaskRepository;
import org.example.tpoprogramacioniii.service.BackTrackingServiceI;
import org.example.tpoprogramacioniii.service.QuickSortServiceI;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ViewConsole {

    private final QuickSortServiceI quickSortService;
    private final BackTrackingServiceI backTrackingService;
    private final TaskRepository taskRepository;
    private final LocationRepository locationRepository;

    private final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter HHMM = DateTimeFormatter.ofPattern("HH:mm");

    public ViewConsole(QuickSortServiceI quickSortService,
                       BackTrackingServiceI backTrackingService,
                       TaskRepository taskRepository,
                       LocationRepository locationRepository) {
        this.quickSortService = quickSortService;
        this.backTrackingService = backTrackingService;
        this.taskRepository = taskRepository;
        this.locationRepository = locationRepository;
    }

    /** Punto de entrada de la vista de consola */
    public void run() {
        while (true) {
            int opcion = elegirOpcionMenu();
            switch (opcion) {
                case 0 -> {
                    System.out.println("Saliendo del programa... 👋");
                    System.exit(0);
                }
                case 1 -> quickSortFlow();
                case 2 -> grafoFlow();
                case 3 -> backtrackingFlow();
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    /* ===================== FLOWS ===================== */

    private void quickSortFlow() {
        List<Task> all = taskRepository.findAll();
        if (all.isEmpty()) {
            System.out.println("No hay tareas cargadas.");
            return;
        }

        System.out.println("\n=== TAREAS (ANTES DEL SORTING) ===");
        printTasks(all);

        String param = quickSortParametro();
        boolean asc = leerAsc();

        List<String> ids = all.stream().map(Task::getId).filter(Objects::nonNull).toList();
        List<String> sortedIds = quickSortService.sortTasksByParam(ids, param, asc);

        Map<String, Task> byId = taskRepository.findAllById(sortedIds).stream()
                .filter(t -> t.getId() != null)
                .collect(Collectors.toMap(Task::getId, Function.identity()));

        System.out.println("\n=== TAREAS (DESPUÉS DEL SORTING) ===");
        for (String id : sortedIds) {
            Task t = byId.get(id);
            if (t != null) printTaskLine(t);
        }
        System.out.println("==================================\n");
    }

    private void grafoFlow() {
        List<Location> nodos = locationRepository.findAll();
        if (nodos.isEmpty()) {
            System.out.println("No hay nodos en la base.");
            return;
        }
        System.out.println("\n=== LISTA DE NODOS (Location) ===");
        for (Location loc : nodos) {
            System.out.printf("• ID: %s | Nombre: %s%n", loc.getId(), loc.getName());
        }
        System.out.println("=================================\n");

        System.out.println("ACA DEBERÍA HABER UN MENÚ PARA ELEGIR NODOS Y ALGORITMOS DE GRAFOS (TODO).");
    }

    private void backtrackingFlow() {
        List<Task> all = taskRepository.findAll();
        if (all.isEmpty()) {
            System.out.println("No hay tareas cargadas para evaluar backtracking.");
            return;
        }

        System.out.println("\n=== TODAS LAS TAREAS ===");
        printTasks(all);

        // Ejecutar selección óptima sin inputs adicionales
        List<String> chosenIds = backTrackingService.selectTasksMaxPriorityNonOverlapping();

        if (chosenIds == null || chosenIds.isEmpty()) {
            System.out.println("\nNo se seleccionaron tareas (resultado vacío).");
            return;
        }

        Map<String, Task> byId = taskRepository.findAllById(chosenIds).stream()
                .filter(t -> t.getId() != null)
                .collect(Collectors.toMap(Task::getId, Function.identity()));

        int totalPriority = 0;
        System.out.println("\n=== TAREAS SELECCIONADAS (BACKTRACKING, SIN SOLAPES) ===");
        for (String id : chosenIds) {
            Task t = byId.get(id);
            if (t == null) continue;
            totalPriority += nz(t.getPriority());
            printTaskLine(t);
        }
        System.out.println("----------------------------------");
        System.out.println("Prioridad total: " + totalPriority);
        System.out.println("==================================\n");
    }

    /* ===================== HELPERS MENÚ ===================== */

    private int elegirOpcionMenu() {
        while (true) {
            try {
                System.out.println("""
                        ELEGIR UNA OPCIÓN:
                        0- SALIR
                        1- SORTING
                        2- GRAFOS
                        3- BACKTRACKING (selección óptima de tareas)
                        """);
                System.out.print("Opción (0-3): ");
                String input = scanner.nextLine().trim();
                int opcion = Integer.parseInt(input);
                if (opcion >= 0 && opcion <= 3) return opcion;
                System.out.println("Opción inválida. Ingresá 0, 1, 2 o 3.\n");
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Ingresá un número (0, 1, 2 o 3).\n");
            }
        }
    }

    private String quickSortParametro() {
        while (true) {
            try {
                System.out.println("""
                        ELIJA UNA OPCIÓN DE SORTING:
                        1- En función de la prioridad
                        2- En función de la hora de inicio
                        3- En función de la hora de fin
                        4- En función del destino (alfabéticamente)
                        """);
                System.out.print("Opción (1-4): ");
                String line = scanner.nextLine().trim();
                int opcion = Integer.parseInt(line);

                return switch (opcion) {
                    case 1 -> "priority";
                    case 2 -> "start";
                    case 3 -> "end";
                    case 4 -> "destino";
                    default -> {
                        System.out.println("Opción inválida. Ingresá un número del 1 al 4.\n");
                        yield null;
                    }
                };
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Ingresá un número del 1 al 4.\n");
            }
        }
    }

    private boolean leerAsc() {
        while (true) {
            System.out.print("¿Orden ascendente? (S/N, default S): ");
            String s = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
            if (s.isEmpty()) return true;
            if (s.startsWith("s") || s.equals("y") || s.equals("yes")) return true;
            if (s.startsWith("n") || s.equals("no")) return false;
            System.out.println("Ingresá S o N.\n");
        }
    }

    /* ===================== PRINT UTILS ===================== */

    private void printTasks(List<Task> tasks) {
        for (Task t : tasks) printTaskLine(t);
    }

    private void printTaskLine(Task t) {
        String id = t.getId();
        String pr = String.valueOf(nz(t.getPriority()));
        String st = t.getTime_window_start() == null ? "--:--" : t.getTime_window_start().format(HHMM);
        String en = t.getTime_window_end() == null ? "--:--" : t.getTime_window_end().format(HHMM);
        String dest = (t.getDestino() != null && t.getDestino().getName() != null)
                ? t.getDestino().getName()
                : "(sin destino)";
        System.out.printf("• %-12s | priority=%-3s | start=%-5s | end=%-5s | destino=%s%n",
                id, pr, st, en, dest);
    }

    private int nz(Integer v) { return v == null ? 0 : v; }
}
