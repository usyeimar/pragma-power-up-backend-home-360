package com.pragma.user360.infrastructure.exceptionshandler;


import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Respuesta estructurada para errores de validación")
public record ValidationExceptionResponse(
        @Schema(description = "Indica si la operación fue exitosa", example = "false")
        Boolean success,

        @Schema(description = "Título general del error", example = "Error de validación en los parámetros")
        String message,

        @Schema(description = "Fecha y hora del error", example = "2023-10-01T12:00:00")
        LocalDateTime timeStamp,

        @Schema(description = "Lista de errores de validación específicos")
        List<ValidationErrorDetail> errors
) {
}
