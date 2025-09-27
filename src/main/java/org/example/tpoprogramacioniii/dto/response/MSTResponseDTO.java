package org.example.tpoprogramacioniii.dto.response;

// Respuesta para los algoritmos que devuelven el Árbol de Expansión Mínimo.
// (Para Prim y Kruskal)

import org.example.tpoprogramacioniii.Enum.AlgorithmEnum;

import java.util.List;

public class MSTResponseDTO {
    private List<SegmentResponseDTO> mstSegments; // La colección de aristas que forman el Árbol de Expansión Mínima.
    private double totalCost; // La suma total del peso de las aristas del MST (ej. costo total de la infraestructura).
    private AlgorithmEnum algorithm; // Nombre del algoritmo utilizado ("PRIM" o "KRUSKAL").
    private Integer nodesCovered; // El número de nodos conectados por el MST.
}
