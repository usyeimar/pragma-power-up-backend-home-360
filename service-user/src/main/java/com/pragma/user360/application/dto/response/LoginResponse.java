package com.pragma.user360.application.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta al iniciar sesión exitosamente")
public record LoginResponse(

        @Schema(description = "Mensaje de éxito", example = "Autenticación exitosa")
        String message,

        @Schema(description = "Token JWT para autenticación subsecuente")
        String token,

        @Schema(description = "Detalles del usuario autenticado", implementation = UserResponse.class)
        UserResponse user
) {
}
