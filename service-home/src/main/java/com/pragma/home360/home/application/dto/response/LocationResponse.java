package com.pragma.home360.home.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de la petición de una ubicación", name = "LocationResponse")
public record LocationResponse(@Schema(description = "Identificador de la ubicación", example = "1") Long id,
                               @Schema(description = "Dirección de la ubicación", example = "Calle 50 #40-20") String address,
                               @Schema(description = "Latitud de la ubicación", example = "6.2518401") Double latitude,
                               @Schema(description = "Longitud de la ubicación", example = "-75.5635925") Double longitude,
                               @Schema(description = "Punto de referencia", example = "Cerca al parque principal") String referencePoint,
                               @Schema(description = "Identificador del barrio al que pertenece la ubicación", example = "1") Long neighborhoodId,
                               @Schema(description = "ID de la ciudad a la que pertenece la ubicación", example = "1") Long cityId
) {
}