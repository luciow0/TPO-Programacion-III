package org.example.tpoprogramacioniii.View;

import org.example.tpoprogramacioniii.Enum.AlgorithmEnum;
import org.example.tpoprogramacioniii.Enum.OptimizationCriteriaEnum;
import org.example.tpoprogramacioniii.model.Location;
import org.example.tpoprogramacioniii.model.Task;
import org.example.tpoprogramacioniii.repository.LocationRepository;
import org.example.tpoprogramacioniii.repository.TaskRepository;
import org.example.tpoprogramacioniii.service.*;
import org.example.tpoprogramacioniii.service.impl.BranchAndBoundServiceImpl;
import org.example.tpoprogramacioniii.service.impl.BreadthFirstSearchServiceImpl;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ViewConsole {

    private final QuickSortServiceI quickSortService;
    private final BackTrackingServiceI backTrackingService;
    private final DijkstraServiceI dijkstraService;
    private final TaskRepository taskRepository;
    private final LocationRepository locationRepository;
    private final BreadthFirstSearchServiceI bfsService;
    private final DepthFirstSearchServiceI depthFirstSearchService;
    private final KruskalServiceI kruskalService;
    private final PrimServiceI primService;
    private final GreedyServiceI greedyService;
    private final HeldKarpServiceI heldKarpService;

    private final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter HHMM = DateTimeFormatter.ofPattern("HH:mm");
    private final BranchAndBoundServiceImpl branchAndBoundServiceImpl;

    public ViewConsole(QuickSortServiceI quickSortService,
                       BackTrackingServiceI backTrackingService,
                       DijkstraServiceI dijkstraService,
                       TaskRepository taskRepository,
                       LocationRepository locationRepository, BreadthFirstSearchServiceI bfsService, DepthFirstSearchServiceI depthFirstSearchService, KruskalServiceI kruskalService, PrimServiceI primService, GreedyServiceI greedyService, HeldKarpServiceI heldKarpService, BranchAndBoundServiceImpl branchAndBoundServiceImpl) {
        this.quickSortService = quickSortService;
        this.backTrackingService = backTrackingService;
        this.dijkstraService = dijkstraService;
        this.taskRepository = taskRepository;
        this.locationRepository = locationRepository;
        this.bfsService = bfsService;
        this.depthFirstSearchService = depthFirstSearchService;
        this.kruskalService = kruskalService;
        this.primService = primService;
        this.greedyService = greedyService;
        this.heldKarpService = heldKarpService;
        this.branchAndBoundServiceImpl = branchAndBoundServiceImpl;
    }

    /** Punto de entrada de la vista de consola */
    public void run() {
        while (true) {
            int opcion = elegirOpcionMenu();
            switch (opcion) {
                case 0 -> {
                    System.out.println("Saliendo del programa... 👋 ");
                    System.exit(0);
                }
                case 1 -> quickSortFlow();
                case 2 -> grafoFlow();
                case 3 -> backtrackingFlow();
                default -> System.out.println("Opción inválida. Por favor seleccione una opcion correcta ");
            }
        }
    }

    /* ===================== FLOWS ===================== */

    private void quickSortFlow() {
        List<Task> all = taskRepository.findAll();
        if (all.isEmpty()) {
            System.out.println("No hay tareas cargadas. ");
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
        // mostrar nodos
        this.mostrarNodos();
        // Submenú: elegir algoritmo de grafos (a partir de AlgorithmEnum)
        while (true) {
            System.out.println("ALGORITMOS DISPONIBLES:");
            AlgorithmEnum[] algs = AlgorithmEnum.values();
            for (int i = 0; i < algs.length; i++) {
                System.out.printf("%d- %s%n", i + 1, algs[i].name());
            }
            System.out.println("0- Volver");
            System.out.print("Opción: ");

            String line = scanner.nextLine().trim();
            int op;
            try {
                op = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Ingresá un número.\n");
                continue;
            }

            if (op == 0) return;
            if (op < 0 || op > algs.length) {
                System.out.println("Opción fuera de rango.\n");
                continue;
            }

            AlgorithmEnum elegido = algs[op - 1];

            switch (elegido) {
                case DIJKSTRA -> runDijkstra();
                case BRANCH_AND_BOUND -> runBranchAndBound();
                case BFS -> runBFS();
                case DFS -> runDFS();
                case MST_KRUSKAL -> runKruskal();
                case MST_PRIM -> runPrim();
                case HELD_KARP -> runHeldKarp();
                case GREEDY -> runGreedy();
                // pide origen/destino y muestra resultado
                default -> {
                    System.out.println("Aún no implementado para: " + elegido.name() + "\n");
                }
            }
        }
    }
    private void mostrarNodos(){
        // Listar nodos (Locations) antes del submenú
        List<Location> nodos = locationRepository.findAll();
        if (nodos.isEmpty()) {
            System.out.println("No hay nodos en la base.");
            return;
        }
        System.out.println("\n=== LISTA DE NODOS (Location) ===");
        for (Location loc : nodos) {
            System.out.printf("• ID: %s | Nombre: %s%n", loc.getId().substring(0,7), loc.getName());
        }
        System.out.println("=================================\n");
    }

    private void runDijkstra() {
        System.out.println("\n== DIJKSTRA ==");
        String origin = readNonEmpty("ID de nodo ORIGEN: ");
        String dest = readNonEmpty("ID de nodo DESTINO: ");

        // Ejecutar Dijkstra con criterio default DISTANCE_KM
        Map<String, Object> result = dijkstraService.calculateOptimalRoute(
                origin, dest, OptimizationCriteriaEnum.DISTANCE_KM
        );

        boolean valid = Boolean.TRUE.equals(result.get("valid"));
        if (!valid) {
            System.out.println("Resultado: " + result.get("message") + "\n");
            return;
        }

        // Mostrar camino y costo
        @SuppressWarnings("unchecked")
        List<String> path = (List<String>) result.get("path");
        Object distance = result.get("distance");

        System.out.println("\nCamino óptimo (por DISTANCE_KM):");
        for (int i = 0; i < path.size(); i++) {
            String id = path.get(i);
            String name = locationRepository.findById(id)
                    .map(Location::getName).orElse("(sin nombre)");
            System.out.printf("  %s%s (%s)%n", i == 0 ? "" : "→ ", id, name);
        }
        System.out.println("Costo total (km): " + distance + "\n");
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


    private void runBranchAndBound() {
        List<Task> all = taskRepository.findAll();
        List<Task> result = branchAndBoundServiceImpl.intervalSchedulingBranchAndBound(all);
        System.out.println("Tareas seleccionadas:");
        result.forEach(System.out::println);
    }

    private void runBFS() {
        System.out.println("\n== BFS ==");
        String origin = readNonEmpty("ID de nodo ORIGEN: ");

        List<String> recorrido = bfsService.bfs(origin);

        if (recorrido.isEmpty()) {
            System.out.println("No se pudo iniciar BFS (nodo inválido o sin conexiones).");
            return;
        }

        System.out.println("Recorrido BFS desde el nodo " + origin + ":");
        for (String id : recorrido) {
            String name = locationRepository.findById(id)
                    .map(Location::getName)
                    .orElse("(sin nombre)");
            System.out.printf(" -> %s (%s)%n", id.substring(0,7), name);
        }
        System.out.println();
    }

    private void runDFS() {
        System.out.println("\n== DEPTH FIRST SEARCH (DFS) ==");
        String origin = readNonEmpty("ID de nodo ORIGEN: ");
        String dest = readNonEmpty("ID de nodo DESTINO: ");

        Map<String, Object> result = depthFirstSearchService.depthFirstSearch(origin, dest);

        if (!(Boolean) result.get("valid")) {
            System.out.println("Resultado: " + result.get("message") + "\n");
            return;
        }

        @SuppressWarnings("unchecked")
        List<String> path = (List<String>) result.get("path");

        System.out.println("\nCamino encontrado (DFS):");
        for (int i = 0; i < path.size(); i++) {
            String id = path.get(i);
            String name = locationRepository.findById(id)
                    .map(Location::getName).orElse("(sin nombre)");
            System.out.printf("  %s%s (%s)%n", i == 0 ? "" : "→ ", id, name);
        }
        System.out.println();
    }

    private void runKruskal() {
        System.out.println("\n== KRUSKAL ==");
        Map<String, Object> result = kruskalService.calculateMST();

        @SuppressWarnings("unchecked")
        List<?> edges = (List<?>) result.get("edges");
        double totalWeight = (double) result.get("totalWeight");

        System.out.println("Aristas seleccionadas en el MST:");
        edges.forEach(System.out::println);
        System.out.println("Peso total del MST: " + totalWeight + " km\n");
    }

    private void runPrim() {
        System.out.println("\n== PRIM ==");
        String origin = readNonEmpty("ID de nodo ORIGEN: ");

        Map<String, Object> result = primService.calculateMST(origin);

        boolean valid = Boolean.TRUE.equals(result.get("valid"));
        if (!valid) {
            System.out.println("Resultado: " + result.get("message") + "\n");
            return;
        }

        @SuppressWarnings("unchecked")
        List<String> mstEdges = (List<String>) result.get("mstEdges");
        Double totalWeight = (Double) result.get("totalWeight");

        System.out.println("\nAristas en el MST:");
        mstEdges.forEach(e -> System.out.println("  " + e));
        System.out.println("Peso total: " + totalWeight + " km\n");
    }


    // ...

    private void runHeldKarp() {
        System.out.println("\n== TSP - Held-Karp (Programación Dinámica) ==");
        List<Task> allTasks = taskRepository.findAll();

        if (allTasks.isEmpty()) {
            System.out.println("No hay tareas cargadas para resolver el TSP.");
            return;
        }

        // 1. Pedir ID de la ubicación de inicio (ej. Base Central)
        this.mostrarNodos();
        String startId = readNonEmpty("ID de nodo de INICIO (ej. Base Central): ");
        Location startLocation = locationRepository.findById(startId).orElse(null);

        if (startLocation == null) {
            System.out.println("Ubicación de inicio no encontrada.");
            return;
        }

        // 2. Elegir el criterio de optimización
        OptimizationCriteriaEnum criteria = chooseOptimizationCriteria();

        // 3. Ejecutar Held-Karp
        Map<String, Object> result = heldKarpService.findOptimalTspRoute(
                allTasks, startId, criteria
        );

        // 4. Mostrar el resultado
        System.out.println("\n=== RESULTADO TSP (Held-Karp) ===");
        boolean valid = Boolean.TRUE.equals(result.get("valid"));

        if (!valid) {
            System.out.println("Mensaje: " + result.get("message"));
            System.out.println("================================\n");
            return;
        }

        @SuppressWarnings("unchecked")
        List<String> pathIds = (List<String>) result.get("path");
        Double totalCost = (Double) result.get("totalCost");

        System.out.println("Criterio: " + criteria.name());
        System.out.println("Costo total óptimo: " + totalCost);
        System.out.println("Ruta óptima (Ciclo Hamiltonian mínimo):");

        for (int i = 0; i < pathIds.size(); i++) {
            String id = pathIds.get(i);
            String name = locationRepository.findById(id)
                    .map(Location::getName).orElse("(sin nombre)");

            String arrow = (i < pathIds.size() - 1) ? " → " : " ↺ (Regreso al inicio)";
            System.out.printf("  %s%s (%s)%n", id, arrow, name);
        }
        System.out.println("================================\n");
    }

    // Método auxiliar para elegir el criterio de optimización
    private OptimizationCriteriaEnum chooseOptimizationCriteria() {
        while (true) {
            System.out.println("\nELIJA CRITERIO DE OPTIMIZACIÓN:");
            OptimizationCriteriaEnum[] criteria = OptimizationCriteriaEnum.values();
            for (int i = 0; i < criteria.length; i++) {
                System.out.printf("%d- %s%n", i + 1, criteria[i].name());
            }
            System.out.print("Opción (1-" + criteria.length + ", default 1): ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) return OptimizationCriteriaEnum.DISTANCE_KM;

            try {
                int op = Integer.parseInt(line);
                if (op >= 1 && op <= criteria.length) return criteria[op - 1];
                System.out.println("Opción inválida.");
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Ingresá un número.");
            }
        }
    }

// ...

    // ... dentro de ViewConsole

    private void runGreedy() {
        List<Task> all = taskRepository.findAll();
        if (all.isEmpty()) {
            System.out.println("No hay tareas cargadas para evaluar el algoritmo Greedy.");
            return;
        }

        System.out.println("\n=== TODAS LAS TAREAS ===");
        printTasks(all);

        // Ejecutar el algoritmo Greedy de selección de intervalos
        List<Task> chosenTasks = greedyService.selectTasksMaxNumberNonOverlapping(all);

        if (chosenTasks.isEmpty()) {
            System.out.println("\nNo se seleccionaron tareas (resultado vacío).");
            return;
        }

        // Calcular la cantidad de tareas seleccionadas
        System.out.println("\n=== TAREAS SELECCIONADAS (GREEDY: Máximo Número de Tareas No Solapadas) ===");
        for (Task t : chosenTasks) {
            printTaskLine(t);
        }
        System.out.println("----------------------------------");
        System.out.println("Número total de tareas seleccionadas: " + chosenTasks.size());
        System.out.println("==================================\n");
    }
    /* ===================== HELPERS MENÚ ===================== */

    private int elegirOpcionMenu() {
        while (true) {
            try {
                System.out.println("""
                        
                        ELEGIR UNA OPCIÓN:
                        0- SALIR
                        1- Opciones para ordenar las tareas 
                        2- Algoritmos aplicables a grafos
                        3- Selección óptima de tareas (Backtracking)
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

    private String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println("No puede ser vacío.\n");
        }
    }

    /* ===================== PRINT UTILS ===================== */

    private void printTasks(List<Task> tasks) {
        for (Task t : tasks) printTaskLine(t);
    }

    private void printTaskLine(Task t) {
        String id = t.getId();
        id = id.substring(0, 7);
        String pr = String.valueOf(nz(t.getPriority()));
        String st = t.getTime_window_start() == null ? "--:--" : t.getTime_window_start().format(HHMM);
        String en = t.getTime_window_end() == null ? "--:--" : t.getTime_window_end().format(HHMM);
        String dest = (t.getDestino() != null && t.getDestino().getName() != null)
                ? t.getDestino().getName()
                : "(sin destino)";
        System.out.printf("• %-12s | Priority = %-3s | Start = %-5s | End = %-5s | Destino = %s%n",
                id, pr, st, en, dest);
    }

    private int nz(Integer v) { return v == null ? 0 : v; }
}
