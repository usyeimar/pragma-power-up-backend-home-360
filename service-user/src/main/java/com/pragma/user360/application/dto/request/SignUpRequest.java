package com.pragma.user360.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Petición para registrar un nuevo usuario")
public record SignUpRequest(
        String email,
        String password,
        String role
) {
}