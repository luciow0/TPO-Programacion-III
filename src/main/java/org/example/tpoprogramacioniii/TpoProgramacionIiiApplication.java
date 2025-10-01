package org.example.tpoprogramacioniii;

import org.example.tpoprogramacioniii.model.Location;
import org.example.tpoprogramacioniii.model.Task;
import org.example.tpoprogramacioniii.repository.LocationRepository;
import org.example.tpoprogramacioniii.repository.TaskRepository;
import org.example.tpoprogramacioniii.service.QuickSortServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootApplication
public class TpoProgramacionIiiApplication implements CommandLineRunner {

    public static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter HHMM = DateTimeFormatter.ofPattern("HH:mm");

    @Autowired private QuickSortServiceI quickSortService;
    @Autowired private TaskRepository taskRepository;
    @Autowired private LocationRepository locationRepository;

    public static void main(String[] args) {
        SpringApplication.run(TpoProgramacionIiiApplication.class, args);
        System.out.println("arrancandovich");
    }

    /** Se ejecuta al iniciar la app */
    @Override
    public void run(String... args) {
        while (true) {
            int opcion = elegirOpcionMenu();
            switch (opcion) {
                case 0 -> {
                    System.out.println("Saliendo del programa... 👋");
                    System.exit(0); // termina la aplicación
                }
                case 1 -> quickSortFlow(); // SORTING
                case 2 -> grafoFlow();     // GRAFOS
            }
        }
    }

    /* ===================== FLOWS ===================== */

    private void quickSortFlow() {
        // 1) Universo de tareas
        List<Task> all = taskRepository.findAll();
        if (all.isEmpty()) {
            System.out.println("No hay tareas cargadas.");
            return;
        }

        // 2) Mostrar tareas PREVIO al sorting
        System.out.println("\n=== TAREAS (ANTES DEL SORTING) ===");
        printTasks(all);

        // 3) Elegir parámetro
        String param = quickSortParametro();

        // 4) Elegir dirección
        boolean asc = leerAsc();

        // 5) IDs a ordenar (todas por default)
        List<String> ids = all.stream().map(Task::getId).filter(Objects::nonNull).toList();

        // 6) Ejecutar QuickSort
        List<String> sortedIds = quickSortService.sortTasksByParam(ids, param, asc);

        // 7) Mostrar resultado
        Map<String, Task> byId = taskRepository.findAllById(sortedIds).stream()
                .filter(t -> t.getId() != null)
                .collect(Collectors.toMap(Task::getId, Function.identity()));

        System.out.println("\n=== TAREAS (DESPUÉS DEL SORTING) ===");
        for (String id : sortedIds) {
            Task t = byId.get(id);
            if (t != null) printTaskLine(t);
        }
        System.out.println("==================================\n");
        this.run();
    }

    private void grafoFlow() {
        // Listar nodos (Locations)
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

        System.out.println("ACA DEBERIA HABER UN MENU PARA ELEGIR LOS NODOS Y VER EL TEMA DE LOS ALGORITMOS DE GRAFOS... XDD");
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
                        """);
                System.out.print("Opción (0-2): ");
                String input = scanner.nextLine().trim();
                int opcion = Integer.parseInt(input);
                if (opcion == 0 || opcion == 1 || opcion == 2) return opcion;
                System.out.println("Opción inválida. Ingresá 1 o 2.\n");
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Ingresá un número (1 o 2).\n");
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
