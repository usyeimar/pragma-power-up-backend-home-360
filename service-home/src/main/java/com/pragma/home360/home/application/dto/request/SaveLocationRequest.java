package com.pragma.home360.home.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Solicitud para guardar una ubicación")
public record SaveLocationRequest(
        @Schema(description = "Latitud de la ubicación", example = "6.2518401") Double latitude,
        @Schema(description = "Longitud de la ubicación", example = "-75.5635925") Double longitude,
        @Schema(description = "Dirección de la ubicación", example = "Calle 123 #45-67") String address,
        @Schema(description = "Punto de referencia", example = "Cerca al parque principal") String referencePoint,
        @Schema(description = "ID del barrio al que pertenece la ubicación", example = "1") Long neighborhoodId,
        @Schema(description = "ID de la ciudad al que pertenece la ubicación", example = "1") Long cityId

) {
}