package com.pragma.home360.home.application.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de la petición de un departamento", name = "DepartmentResponse")
public record DepartmentResponse(@Schema(description = "ID del departamento", example = "1") Long id,
                                 @Schema(description = "Nombre del departamento", example = "Cundinamarca") String name,
                                 @Schema(description = "Descripción del departamento", example = "Departamento de Cundinamarca") String description) {
}