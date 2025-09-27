package org.example.tpoprogramacioniii.dto.response;

// Respuesta para cualquier algoritmo que devuelva una
// secuencia de nodos/aristas.
//  (Para Dijkstra, BFS, DFS, TSP, DP, Backtracking)

import org.example.tpoprogramacioniii.Enum.AlgorithmEnum;

import java.util.List;

public class RouteResponseDTO {
    private List<LocationResponseDTO> path;
    // Secuencia ordenada de los nodos visitados (la ruta).
    private List<SegmentResponseDTO> segmentsUsed;
    // Lista de los segmentos de la carretera que componen la ruta.
    private double totalDistanceKm;
    // Suma total de las distancias de los segmentos
    private double totalTimeMin;
    // Suma total de los tiempos de los segmentos
    private AlgorithmEnum algorithm; // Nombre del algoritmo utilizado (ej. "DIJKSTRA", "A_STAR").

}
