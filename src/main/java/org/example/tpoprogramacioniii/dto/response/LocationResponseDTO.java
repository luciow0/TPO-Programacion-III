package org.example.tpoprogramacioniii.dto.response;

// Entidad base. Se usa para representar un nodo en las respuestas

import org.springframework.data.annotation.Id;

public class LocationResponseDTO {
    @Id
    private String id;
    private String name;
    private double latitude;
    private double longitude;
}
