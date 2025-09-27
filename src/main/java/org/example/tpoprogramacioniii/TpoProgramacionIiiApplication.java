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

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.GraphDatabase;

@SpringBootApplication
public class TpoProgramacionIiiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TpoProgramacionIiiApplication.class, args);

        // URI examples: "neo4j://localhost", "neo4j+s://xxx.databases.neo4j.io"
        final String dbUri = "neo4j+s://f140e33c.databases.neo4j.io";
        final String dbUser = "neo4j";
        final String dbPassword = "neo4jadmin";

        try (var driver = GraphDatabase.driver(dbUri, AuthTokens.basic(dbUser, dbPassword))) {
            driver.verifyConnectivity();
            System.out.println("Connection established.");
        }
    }

}
