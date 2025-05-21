package com.pragma.home360.home.application.dto.request;

import com.pragma.home360.home.infrastructure.entities.enums.PropertyPublicationStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SavePropertyRequest(
        @NotBlank(message = "El nombre de la propiedad no puede estar vacío.")
        @Size(min = 5, max = 255, message = "El nombre debe tener entre 5 y 255 caracteres.")
        String name,

        @Size(max = 2000, message = "La descripción no puede exceder los 2000 caracteres.")
        String description,

        @NotNull(message = "La cantidad de cuartos es obligatoria.")
        @Positive(message = "La cantidad de cuartos debe ser un número positivo.")
        Integer numberOfRooms,

        @NotNull(message = "La cantidad de baños es obligatoria.")
        @Positive(message = "La cantidad de baños debe ser un número positivo.")
        Integer numberOfBathrooms,

        @NotNull(message = "El precio es obligatorio.")
        @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor que cero.")
        BigDecimal price,

        @FutureOrPresent(message = "La fecha de publicación activa debe ser hoy o una fecha futura.")
        LocalDate activePublicationDate,


        @NotNull(message = "El ID de la categoría es obligatorio.")
        Integer categoryId,

        @NotNull(message = "El ID de la localidad es obligatorio.")
        Integer locationId
) {
}