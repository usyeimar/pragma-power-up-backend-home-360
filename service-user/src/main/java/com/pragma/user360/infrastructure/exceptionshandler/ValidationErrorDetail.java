package com.pragma.user360.infrastructure.exceptionshandler;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Detalle de un error de validación específico")
public record ValidationErrorDetail(
        @Schema(description = "Campo que falló la validación", example = "email")
        String field,

        @Schema(description = "Mensaje descriptivo del error de validación", example = "Formato de correo electrónico inválido")
        String detail,

        @Schema(description = "Valor rechazado que causó el error (si aplica)", example = "juan.perez@")
        String rejectedValue
) {
    public ValidationErrorDetail(String field, String detail) {
        this(field, detail, null);
    }
}
