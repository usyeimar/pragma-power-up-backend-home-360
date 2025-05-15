package com.pragma.home360.home.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Respuesta de la petición de guardar una categoría", name = "SaveCategoryResponse")
public record SaveCategoryResponse(@Schema(description = "Identificador de la categoría creada", example = "1") Long id,
                                   @Schema(description = "Mensaje de la respuesta", example = "Categoría creada correctamente") String message,
                                   @Schema(description = "Fecha y hora de la respuesta", example = "2021-08-12T12:00:00") LocalDateTime time) {
}
