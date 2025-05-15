package com.pragma.home360.home.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de la petición de una ciudad", name = "CityResponse")
public record CityResponse(@Schema(description = "Identificador de la ciudad", example = "1") Long id,
                           @Schema(description = "Nombre de la ciudad", example = "Madrid") String name,
                           @Schema(description = "Descripción de la ciudad", example = "Capital de España") String description,
                           @Schema(description = "Identificador del departamento al que pertenece la ciudad", example = "1") Long departmentId) {

    @Override
    public String toString() {
        return "CityResponse{" + "id=" + id + ", name='" + name + '\'' + ", description='" + description + '\'' + ", departmentId=" + departmentId + '}';
    }
}