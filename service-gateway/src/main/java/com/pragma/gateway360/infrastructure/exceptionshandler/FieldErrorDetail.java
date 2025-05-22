package com.pragma.gateway360.infrastructure.exceptionshandler;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Detalle de un error de validación de campo específico.")
public record FieldErrorDetail(
        @Schema(description = "Nombre del campo que falló la validación.", example = "name")
        String field,
        @Schema(description = "Mensaje describiendo el error de validación para el campo.", example = "El nombre no puede estar vacío.")
        String message,
        @Schema(description = "Valor rechazado que causó el error (si aplica).", example = "null")
        Object rejectedValue
) {
}
