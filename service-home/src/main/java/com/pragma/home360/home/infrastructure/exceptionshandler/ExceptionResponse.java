package com.pragma.home360.home.infrastructure.exceptionshandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Respuesta de error genérica y estructurada")
@JsonInclude(JsonInclude.Include.NON_NULL) // No incluir campos nulos en el JSON
public record ExceptionResponse(
        @Schema(description = "Indica si la operación fue exitosa (siempre false para errores)", example = "false")
        Boolean success,

        @Schema(description = "Código de error único para identificar el tipo de error.", example = "ERR100")
        String errorCode,

        @Schema(description = "Mensaje general describiendo el error.", example = "Error de validación en los parámetros de entrada.")
        String message,

        @Schema(description = "Fecha y hora del error, en formato ISO 8601.", example = "2023-10-01T12:00:00")
        LocalDateTime timestamp,

        @Schema(description = "Detalles específicos del error. Puede ser un String o una lista de FieldErrorDetail para errores de validación.",
                example = "[{\"field\":\"name\",\"message\":\"El nombre no puede estar vacío\",\"rejectedValue\":null}]")
        Object details, // Para errores de validación, será List<FieldErrorDetail>; para otros, un String.

        @Schema(description = "Lista detallada de errores de campo (solo para errores de validación).")
        List<FieldErrorDetail> fieldErrors // Específico para errores de validación múltiple
) {
    // Constructor para errores generales (sin fieldErrors)
    public ExceptionResponse(Boolean success, String errorCode, String message, LocalDateTime timestamp, String details) {
        this(success, errorCode, message, timestamp, details, null);
    }

    // Constructor para errores de validación (con fieldErrors)
    public ExceptionResponse(Boolean success, String errorCode, String message, LocalDateTime timestamp, List<FieldErrorDetail> fieldErrors) {
        this(success, errorCode, message, timestamp, null, fieldErrors);
    }
}
