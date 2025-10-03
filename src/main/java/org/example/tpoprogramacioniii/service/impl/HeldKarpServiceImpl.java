package org.example.tpoprogramacioniii.service.impl;

import org.example.tpoprogramacioniii.Enum.OptimizationCriteriaEnum;
import org.example.tpoprogramacioniii.model.Location;
import org.example.tpoprogramacioniii.model.Task;
import org.example.tpoprogramacioniii.service.DijkstraServiceI;
import org.example.tpoprogramacioniii.service.HeldKarpServiceI;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HeldKarpServiceImpl implements HeldKarpServiceI {

    private final DijkstraServiceI dijkstraService;

    // Inyecta el servicio de Dijkstra para calcular los costos entre los nodos del TSP
    public HeldKarpServiceImpl(DijkstraServiceI dijkstraService) {
        this.dijkstraService = dijkstraService;
    }

    // Estructuras de soporte para Held-Karp
    private double[][] costMatrix; // Matriz de costos (distancias) entre las ubicaciones de las tareas.
    private double[][] dp; // dp[mask][j] = costo mínimo de la ruta que comienza en 'start', visita todos los nodos en 'mask' y termina en 'j'.
    private int[][] parent; // Para reconstruir el camino.

    /**
     * @inheritDoc
     */
    @Override
    public Map<String, Object> findOptimalTspRoute(List<Task> tasks, String startLocationId, OptimizationCriteriaEnum criteria) {
        // 1. Preparar las ubicaciones a visitar (incluyendo el punto de inicio)
        List<String> taskLocationIds = tasks.stream()
                .map(Task::getDestino)
                .filter(Objects::nonNull)
                .map(Location::getId)
                .collect(Collectors.toList());

        // Asegurar que solo haya IDs únicos, el ID de inicio es obligatorio
        Set<String> uniqueIdsSet = new LinkedHashSet<>();
        uniqueIdsSet.add(startLocationId); // Base/Inicio siempre en la posición 0
        uniqueIdsSet.addAll(taskLocationIds);

        List<String> nodes = new ArrayList<>(uniqueIdsSet);
        int N = nodes.size();

        if (N <= 1) {
            return Map.of(
                    "path", N == 1 ? List.of(nodes.get(0)) : List.of(),
                    "totalCost", 0.0,
                    "valid", true,
                    "message", N <= 1 ? "Solo una o ninguna ubicación. No hay ruta TSP que calcular." : "Inicio y/o tareas inválidas."
            );
        }

        // 2. Construir la Matriz de Costos (Costo de i a j)
        if (!buildCostMatrix(nodes, criteria)) {
            return Map.of(
                    "path", List.of(),
                    "totalCost", 0.0,
                    "valid", false,
                    "message", "No se pudo construir la matriz de costos (algunos caminos son inalcanzables o el servicio falló)."
            );
        }

        // 3. Inicializar DP y Parent
        int numStates = 1 << N; // 2^N estados
        this.dp = new double[numStates][N];
        this.parent = new int[numStates][N];
        for (double[] row : dp) Arrays.fill(row, Double.MAX_VALUE);
        for (int[] row : parent) Arrays.fill(row, -1);

        // El inicio siempre es el nodo en el índice 0 (startLocationId)
        int startIndex = 0;

        // El costo de ir del inicio (0) al inicio (0) con solo el bit 0 encendido es 0
        dp[1 << startIndex][startIndex] = 0;

        // 4. Algoritmo de Held-Karp (Programación Dinámica)
        // Recorre los estados (máscaras) de menor a mayor
        for (int mask = 1; mask < numStates; mask++) {
            for (int last = 0; last < N; last++) {
                // Si 'last' no está en la máscara, o el estado actual no ha sido alcanzado, continúa
                if ((mask & (1 << last)) == 0 || dp[mask][last] == Double.MAX_VALUE) continue;

                // Intenta ir a un 'next' nodo que no esté en la máscara
                for (int next = 0; next < N; next++) {
                    if ((mask & (1 << next)) == 0) {
                        int nextMask = mask | (1 << next);
                        double costToNext = costMatrix[last][next];
                        double newCost = dp[mask][last] + costToNext;

                        if (newCost < dp[nextMask][next]) {
                            dp[nextMask][next] = newCost;
                            parent[nextMask][next] = last;
                        }
                    }
                }
            }
        }

        // 5. Encontrar el costo total mínimo y el último nodo antes de volver al inicio (0)
        int finalMask = numStates - 1; // Máscara donde todos los bits están encendidos
        double minTotalCost = Double.MAX_VALUE;
        int lastNodeIndex = -1;

        // Recorre todos los nodos 'j' para encontrar el mejor camino que termine en 'j' y luego regrese al inicio (0)
        for (int j = 1; j < N; j++) { // Empezar desde 1, ya que el inicio es 0
            double costToStart = costMatrix[j][startIndex];
            double totalCost = dp[finalMask][j] + costToStart;

            if (totalCost < minTotalCost) {
                minTotalCost = totalCost;
                lastNodeIndex = j;
            }
        }

        if (lastNodeIndex == -1 || minTotalCost == Double.MAX_VALUE) {
            return Map.of(
                    "path", List.of(),
                    "totalCost", 0.0,
                    "valid", false,
                    "message", "No se encontró un ciclo que visite todas las ubicaciones y regrese al inicio."
            );
        }

        // 6. Reconstruir la ruta óptima
        List<String> optimalPath = new LinkedList<>();
        int currentMask = finalMask;
        int currentNode = lastNodeIndex;

        // 6.1. Recorrer hacia atrás desde el último nodo hasta el inicio
        while (currentNode != -1) {
            optimalPath.add(0, nodes.get(currentNode));
            int prevNode = parent[currentMask][currentNode];

            // Si el nodo actual no es el inicial, se actualiza la máscara para el siguiente paso
            if (currentNode != startIndex) {
                currentMask = currentMask ^ (1 << currentNode); // Apaga el bit del nodo actual
            }
            currentNode = prevNode;
        }

        // 6.2. Agregar el inicio al final para completar el ciclo TSP
        optimalPath.add(nodes.get(startIndex));

        // 7. Retornar el resultado
        return Map.of(
                "path", optimalPath,
                "totalCost", round(minTotalCost),
                "valid", true,
                "message", "Ruta TSP óptima calculada por Held-Karp."
        );
    }

    /**
     * Construye la matriz de costos (distancia/tiempo/costo) entre todos los pares de nodos.
     * Utiliza Dijkstra para encontrar la ruta más corta/barata entre cada par.
     */
    private boolean buildCostMatrix(List<String> nodes, OptimizationCriteriaEnum criteria) {
        int N = nodes.size();
        this.costMatrix = new double[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i == j) {
                    costMatrix[i][j] = 0.0;
                    continue;
                }

                // Usar el servicio de Dijkstra para obtener el costo del camino (i -> j)
                Map<String, Object> result = dijkstraService.calculateOptimalRoute(
                        nodes.get(i), nodes.get(j), criteria
                );

                boolean valid = Boolean.TRUE.equals(result.get("valid"));
                if (valid) {
                    // El costo es el valor de 'distance' (que en realidad es el costo según el criterio)
                    double cost = (Double) result.get("distance");
                    costMatrix[i][j] = cost;
                } else {
                    // Si no hay camino, Held-Karp no se puede completar (costo infinito)
                    costMatrix[i][j] = Double.MAX_VALUE;
                }
            }
        }

        // Verifica si al menos existe un camino entre todos los nodos para evitar un ciclo trivial
        // En este caso, asumiremos que si hay un camino de i->j, Held-Karp puede intentar completarlo.
        return true;
    }

    /**
     * Redondea un Double a 2 decimales.
     */
    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}