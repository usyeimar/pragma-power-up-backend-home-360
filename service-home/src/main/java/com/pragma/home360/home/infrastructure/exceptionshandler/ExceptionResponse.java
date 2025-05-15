package com.pragma.home360.home.infrastructure.exceptionshandler;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Respuesta de error genérica")
public record ExceptionResponse(
        @Schema(description = "Indica si la operación fue exitosa", example = "false") Boolean success,
        @Schema(description = "Título del error", example = "Error de validación en los parámetros") String message,
        @Schema(description = "Fecha y hora del error, en formato ISO 8601", example = "2023-10-01T12:00:00") LocalDateTime timeStamp,
        @Schema(description = "Detalles del error", example = "Campo 'name': El campo no puede estar vacío") String details) {
}
