package com.pragma.home360.home.application.dto.response;

import com.pragma.home360.home.infrastructure.entities.enums.PropertyPublicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Respuesta detallada de la propiedad")
public record PropertyResponse(
        @Schema(description = "ID de la propiedad", example = "1") Long id,
        @Schema(description = "Nombre de la propiedad", example = "Hermosa Casa con Vista al Lago") String name,
        @Schema(description = "Descripción detallada de la propiedad", example = "Una casa espaciosa y moderna con todas las comodidades...") String description,
        @Schema(description = "Número de habitaciones", example = "3") Integer numberOfRooms,
        @Schema(description = "Número de baños", example = "2") Integer numberOfBathrooms,
        @Schema(description = "Precio de la propiedad", example = "250000.00") BigDecimal price,
        @Schema(description = "Fecha de publicación activa", example = "2024-05-20") LocalDate activePublicationDate,
        @Schema(description = "Estado de la publicación de la propiedad") PropertyPublicationStatus publicationStatus,
        @Schema(description = "Fecha de creación de la publicación", example = "2024-01-15T10:30:00") LocalDateTime createdAt,
        @Schema(description = "Fecha de última actualización", example = "2024-05-10T15:00:00") LocalDateTime updatedAt,

        @Schema(description = "Detalles de la categoría de la propiedad") CategoryResponse category,
        @Schema(description = "Detalles de la ubicación de la propiedad") LocationResponse location,

        @Schema(description = "Lista de URLs de imágenes de la propiedad", example = "[\"url1.jpg\", \"url2.png\"]") List<String> images
) {
}
