package com.pragma.user360.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Respuesta al registrar un usuario")
public record UserResponse(
        @Schema(description = "ID del usuario creado", example = "1")
        Long id,

        @Schema(description = "Nombre del usuario", example = "Juan")
        @JsonProperty("first_name")
        String firstName,

        @Schema(description = "Apellido del usuario", example = "Pérez")
        @JsonProperty("last_name")
        String lastName,

        @Schema(description = "Correo electrónico del usuario", example = "juan.perez@ejemplo.com")
        String email,

        @Schema(description = "Rol asignado al usuario", example = "VENDEDOR")
        String role,

        @Schema(description = "Indica si el usuario está activo", example = "true")
        @JsonProperty("is_active")
        Boolean active,

        @Schema(description = "Fecha de creación del usuario", example = "2023-10-15T10:30:00")
        @JsonProperty("created_at")
        LocalDateTime createdAt
) {
}