package com.pragma.user360.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Petición para iniciar sesión")
public record SignInRequest(
        @NotBlank(message = "El correo electrónico es obligatorio")
        @Email(message = "Formato de correo electrónico inválido")
        @Schema(description = "Correo electrónico del usuario", example = "juan.perez@ejemplo.com", requiredMode = Schema.RequiredMode.REQUIRED)
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        @Schema(description = "Contraseña del usuario", example = "Cl4v3S3gur4!", requiredMode = Schema.RequiredMode.REQUIRED)
        String password
) {
}
