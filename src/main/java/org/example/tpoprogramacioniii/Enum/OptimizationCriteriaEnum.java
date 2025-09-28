package org.example.tpoprogramacioniii.Enum;

// Para algoritmos de ruta (dijkstra) y MST (prim/kruskal)

public enum OptimizationCriteriaEnum {
    TIME_MIN("time_min"), // minimizar el tiempo de total de viaje
    COST_FUEL("cost_fuel"), // minimzar el costo monetario total (ej. combustible)
    DISTANCE_KM("distance_km"), // minimizar la longitud total de la ruta/red

    // para algoritmos de recorrido sin peso (BFS/DFS)
    SEGMENTS_COUNT(null); // minimizar el numero de segmentos/paradas (usado en BFS)
    // indica que para BFS, no hay una propiedad de peso en neo4j, solo se cuenta el numero de aristas

    private final String neo4jProperity;

    OptimizationCriteriaEnum(String neo4jProperity) {
        this.neo4jProperity = neo4jProperity;
    }

    public String getNeo4jProperity() {
        return neo4jProperity;
    }
}


// los elementos listados no son solo constantes, sino instancias de la clase CriteriaEnum
// por lo que, poseen la capacidad de que cada valor tenga sus propios campos y metodos, haciendolos mas poderosos
// lo que esta entre parentesis despues de por ej. ENUM("time_min") es el valor que se le esta pasando
// al constructor.

// el campo privado neo4jProperty, cada una de las constantes ahora tiene
// su propia copia del campo neo4jProperty,