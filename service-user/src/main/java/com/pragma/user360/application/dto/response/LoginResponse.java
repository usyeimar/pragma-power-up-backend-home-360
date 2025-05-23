package com.pragma.user360.application.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Respuesta al iniciar sesión exitosamente")
public record LoginResponse(

        @JsonProperty("token_type")
        @Schema(description = "Tipo de token.", example = "Bearer")
        String tokenType,

        @JsonProperty("access_token")
        @Schema(description = "Token de acceso JWT.", example
                = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
        String accessToken,


        @JsonProperty("expires_in")
        @Schema(description = "Tiempo de vida del token en milisegundos desde su emisión.", example = "86400000")
        Long expiresIn,

        @JsonProperty("issued_at")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        @Schema(description = "Fecha y hora de emisión del token (UTC).", example = "2024-05-22T10:15:30Z")
        LocalDateTime issuedAt,

        @JsonProperty("user_profile")
        @Schema(description = "Detalles del perfil del usuario autenticado.")
        UserResponse userProfile
) {
}
