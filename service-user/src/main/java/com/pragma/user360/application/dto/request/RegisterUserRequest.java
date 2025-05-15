package com.pragma.user360.application.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.Date;

@Schema(description = "Petición para registrar un nuevo usuario")
public record RegisterUserRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
        @Schema(description = "Nombre del usuario", example = "Juan")
        String firstName,

        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 50, message = "El apellido no puede exceder los 50 caracteres")
        @Schema(description = "Apellido del usuario", example = "Pérez")
        String lastName,

        @NotBlank(message = "El documento de identidad es obligatorio")
        @Pattern(regexp = "^[0-9]+$", message = "El documento debe contener solo números")
        @Size(max = 20, message = "El documento no puede exceder los 20 caracteres")
        @Schema(description = "Documento de identidad del usuario", example = "1098765432")
        String documentId,

        @NotBlank(message = "El número de teléfono es obligatorio")
        @Pattern(regexp = "^\\+?[0-9]{7,12}$", message = "Formato de teléfono inválido. Debe contener entre 7 y 12 números, opcionalmente con un + al inicio")
        @Size(max = 13, message = "El teléfono no puede exceder los 13 caracteres")
        @Schema(description = "Número de teléfono del usuario", example = "+573005698325")
        String phoneNumber,

        @NotNull(message = "La fecha de nacimiento es obligatoria")
        @Past(message = "La fecha de nacimiento debe ser en el pasado")
        @JsonFormat(pattern = "yyyy-MM-dd")
        @Schema(description = "Fecha de nacimiento del usuario (formato ISO)", example = "1990-01-15")
        Date birthDate,

        @NotBlank(message = "El correo electrónico es obligatorio")
        @Email(message = "Formato de correo electrónico inválido")
        @Size(max = 100, message = "El correo no puede exceder los 100 caracteres")
        @Schema(description = "Correo electrónico del usuario", example = "juan.perez@ejemplo.com")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        @Schema(description = "Contraseña del usuario (mínimo 8 caracteres)", example = "Cl4v3S3gur4!")
        String password
) {
}