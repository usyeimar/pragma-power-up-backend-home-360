package com.pragma.home360.home.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de la categoría")
public record CategoryResponse(@Schema(description = "ID de la categoría", example = "1") Long id,
                               @Schema(description = "Nombre de la categoría", example = "Casa") String name,
                               @Schema(description = "Descripción de la categoría", example = "Casa de campo") String description) {
}
