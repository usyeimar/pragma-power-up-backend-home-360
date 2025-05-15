package com.pragma.home360.home.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Petición para guardar una categoría")
public record SaveCategoryRequest(
        @Schema(description = "Nombre de la categoría", example = "Casa de campo") String name,
        @Schema(description = "Descripción de la categoría", example = "Casa de campo con piscina") String description) {
}
