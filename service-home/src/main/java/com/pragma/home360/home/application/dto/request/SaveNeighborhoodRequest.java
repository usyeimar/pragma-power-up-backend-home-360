package com.pragma.home360.home.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Petición para guardar un barrio")
public record SaveNeighborhoodRequest(@Schema(description = "Nombre del barrio", example = "El Poblado") String name,
                                      @Schema(description = "Descripción del barrio", example = "Barrio residencial de alta categoría") String description,
                                      @Schema(description = "Identificador de la ciudad a la que pertenece el barrio", example = "1") Long cityId) {
}