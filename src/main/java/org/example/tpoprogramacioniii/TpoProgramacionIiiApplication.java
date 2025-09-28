package org.example.tpoprogramacioniii;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Idea Central: Sistema de Optimización
// de Rutas y Redes (Logística o Transporte Urbano)
// Este sistema simularía la gestión de una red de transporte
// (como un servicio de paquetería o una aplicación de movilidad)
// donde las ubicaciones son nodos y las carreteras/caminos
// son aristas con pesos (distancia, tiempo, costo).

// En las intercaces de los algoritmos (paquete service)
// estan las justificaciones de su uso

// LocationController (Location):
//|--> BFS
//|--> DFS
//|--> Dijkstra
//
//SegmentController (Segment):
//|--> Prim
//|--> Kruskal
//|--> Greedy
//
//TaskController (Task):
//|--> HeldKarp (PD)
//|--> BackTracking

// Quicksort usado por quien lo necesite

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.GraphDatabase;

@SpringBootApplication
public class TpoProgramacionIiiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TpoProgramacionIiiApplication.class, args);
        System.out.println("arrancandovich");
    }

}
