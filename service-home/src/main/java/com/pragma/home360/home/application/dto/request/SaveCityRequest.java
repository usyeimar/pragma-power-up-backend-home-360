package com.pragma.home360.home.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Petición para guardar una ciudad")
public record SaveCityRequest(@Schema(description = "Nombre de la ciudad", example = "Madrid") String name,
                              @Schema(description = "Descripción de la ciudad", example = "Capital de España") String description,
                              @Schema(description = "Identificador del departamento al que pertenece la ciudad", example = "1") Long departmentId) {
}