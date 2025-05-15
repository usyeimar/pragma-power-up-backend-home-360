package com.pragma.home360.home.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record SaveDepartmentRequest(@Schema(description = "Nombre del departamento", example = "Antioquia") String name,
                                    @Schema(description = "Descripción del departamento", example = "Departamento en Colombia") String description) {
}