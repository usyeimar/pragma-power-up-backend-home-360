package com.pragma.home360.home.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de barrio")
public record NeighborhoodResponse(@Schema(description = "Identificador del barrio", example = "1") Long id,
                                   @Schema(description = "Nombre del barrio", example = "El Poblado") String name,
                                   @Schema(description = "Descripción del barrio", example = "Barrio residencial de alta categoría") String description,
                                   CityResponse city
) {
}