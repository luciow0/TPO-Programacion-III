package org.example.tpoprogramacioniii.service;

// Implementacion en el sistema:
// Búsqueda del camino con la menor cantidad de paradas
// (sin considerar la distancia).

// Uso y justificacion:
// Exploración de la red, por ejemplo,
// para encontrar si existe una conexión a un nodo
// en particular o el camino más corto en número de aristas.

import java.util.List;

public interface BreadthFirstSearchServiceI {

    List<String> bfs(String originId);
}
