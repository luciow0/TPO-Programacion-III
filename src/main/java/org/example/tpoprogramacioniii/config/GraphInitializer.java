package org.example.tpoprogramacioniii.config;

import org.example.tpoprogramacioniii.Enum.AreaEnum;
import org.example.tpoprogramacioniii.model.Location;
import org.example.tpoprogramacioniii.model.Segment;
import org.example.tpoprogramacioniii.model.Task;
import org.example.tpoprogramacioniii.repository.LocationRepository;
import org.example.tpoprogramacioniii.repository.SegmentRepository;
import org.example.tpoprogramacioniii.repository.TaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

// funciones que componen esta clase:
// initDatabase
// createRandomSegment
// createRandomTask

@Configuration
public class GraphInitializer {

    // Nombres base para 20 ubicaciones
    private static final String[] LOCATION_NAMES = {
            "Base Central", "Depósito Norte", "Planta Sur", "HUB Este", "Oficina Oeste",
            "Punto A1", "Punto B2", "Punto C3", "Punto D4", "Punto E5",
            "Terminal F", "Estación G", "Centro H", "Lugar I", "Zona J",
            "Sector K", "Área L", "Almacén M", "Puerto N", "Ciudad O"
    };

    private static final AreaEnum[] AREAS = AreaEnum.values();
    private static final int NUM_LOCATIONS = LOCATION_NAMES.length; // 20 ubicaciones
    private static final Random RANDOM = new Random();

    @Bean
    public CommandLineRunner initDatabase(
            LocationRepository locationRepository,
            SegmentRepository segmentRepository,
            TaskRepository taskRepository
    ) {
        return args -> {
            System.out.println("--- Iniciando Script de Inicialización de Grafo Neo4j ---");

            // 1. Limpieza de datos existentes (TRUNCADO)
            // Se limpian primero las tareas y segmentos para evitar problemas de referencias
            taskRepository.deleteAll();
            segmentRepository.deleteAll();
            locationRepository.deleteAll();
            System.out.println("Base de datos de Neo4j truncada (Location, Segment, Task).");


            // 2. Creación y guardado de Nodos Location (20 nodos)
            List<Location> locations = new ArrayList<>();
            for (int i = 0; i < NUM_LOCATIONS; i++) {
                String id = UUID.randomUUID().toString();
                String name = LOCATION_NAMES[i];
                // Asigna un área de forma aleatoria
                AreaEnum area = AREAS[RANDOM.nextInt(AREAS.length)];
                Location loc = new Location(id, name, area);
                locations.add(loc);
            }
            // Guardar todas las ubicaciones en Neo4j
            // locations = locationRepository.saveAll(locations);
            for(int i = 0; i < locations.size(); i++){
                Location loc = locations.get(i);
                locationRepository.save(loc);
            }
            // locationRepository.saveAll(locations);
            System.out.printf("Creadas y guardadas %d ubicaciones (nodos).\n", locations.size());


            // 3. Creación y guardado de Relaciones Segment (Aristas)
            List<Segment> segments = new ArrayList<>();

            // Creación de una red densa (aproximadamente 2 a 4 conexiones por nodo)
            for (int i = 0; i < NUM_LOCATIONS; i++) {
                Location loc1 = locations.get(i);

                int connectionsToMake = 2 + RANDOM.nextInt(3); // Crea entre 2 y 4 aristas salientes

                for (int j = 0; j < connectionsToMake; j++) {
                    // Selecciona un nodo destino aleatorio distinto del nodo de origen
                    int targetIndex = (i + RANDOM.nextInt(NUM_LOCATIONS - 1) + 1) % NUM_LOCATIONS;
                    Location loc2 = locations.get(targetIndex);

                    // Crea el segmento unidireccional
                    segments.add(createRandomSegment(loc1, loc2));

                    // Añade el segmento inverso (bidireccional) de forma aleatoria (50% de probabilidad)
                    boolean isBidirectional = RANDOM.nextBoolean();
                    if (isBidirectional) {
                        segments.add(createRandomSegment(loc2, loc1));
                    }
                }
            }

            // 4. Creación de Tareas (Tasks)
            List<Task> tasks = new ArrayList<>();
            int numTasks = 8; // Un tamaño de conjunto de tareas manejable para Held-Karp
            for (int i = 0; i < numTasks; i++) {
                // Asigna la tarea a una ubicación de destino aleatoria
                Location taskLocation = locations.get(RANDOM.nextInt(NUM_LOCATIONS));
                Task task = createRandomTask(taskLocation, i);
                tasks.add(task);
            }

            // Guardar Segmentos y Tareas
            segmentRepository.saveAll(segments);
            taskRepository.saveAll(tasks);

            System.out.printf("Creados y guardados %d segmentos (aristas) que forman la red.\n", segments.size());
            System.out.printf("Creadas y guardadas %d tareas para el Problema del Vendedor Viajero (TSP).\n", tasks.size());
            System.out.println("--- Grafo de logística inicializado correctamente ---");

            System.out.println(" ");
            System.out.println("Datos generados: ");
            System.out.println("Locations ");
            for(Location location : locations) System.out.println("localidad: " + location.getName() + " Area: " + location.getArea());
            System.out.println("Aristas ");
            for(Segment segment : segments) System.out.println("distancia: " + segment.getDistanceKm() + " Tiempo: " + segment.getTimeMin());
            System.out.println("Tasks ");
            for(Task task : tasks) System.out.println("destino: " + task.getDestino().getName() + " Prioridad: " + task.getPriority());

        };
    }

    /**
     * Crea un Segmento con valores aleatorios (distancia, tiempo, costo de combustible).
     */
    private Segment createRandomSegment(Location from, Location to) {
        String id = UUID.randomUUID().toString();
        // Generar valores realistas para un tramo de red
        // Distancia: entre 5 y 50 km
        double distanceKm = ThreadLocalRandom.current().nextDouble(5.0, 50.0);
        // Tiempo: ~30-70 km/h de velocidad promedio (distancia / velocidad)
        double timeMin = distanceKm / ThreadLocalRandom.current().nextDouble(0.5, 1.2);
        // Costo: basado en la distancia
        double costFuel = distanceKm * ThreadLocalRandom.current().nextDouble(0.1, 0.5);

        // La bidireccionalidad se gestiona en el bucle principal, aquí es false por defecto
        return new Segment(id, from.getId(), to.getId(),
                round(distanceKm),
                round(timeMin),
                round(costFuel),
                false);
    }

    /**
     * Crea una Tarea con prioridad y ventana de tiempo aleatoria usando LocalTime.
     */
    private Task createRandomTask(Location destination, int index) {
        String id = UUID.randomUUID().toString();
        int priority = index + 1;

        // Crear ventanas de tiempo dentro de un día de trabajo (ej. 8:00 a 20:00)
        // La ventana de inicio va de 8:00 AM (hora 8) a 16:00 PM (hora 16)
        int startHour = 8 + RANDOM.nextInt(9);
        LocalTime timeWindowStart = LocalTime.of(startHour, 0);

        // La ventana de fin ocurre 1 a 3 horas después
        LocalTime timeWindowEnd = timeWindowStart.plusHours(1 + RANDOM.nextInt(3));

        return new Task(id, priority, timeWindowStart, timeWindowEnd, destination);
    }

    /**
     * Redondea un Double a 2 decimales para simular valores de medición reales.
     */
    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
