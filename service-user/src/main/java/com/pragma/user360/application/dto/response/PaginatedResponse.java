package com.pragma.user360.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;


@Schema(description = "Respuesta paginada genérica")
public record PaginatedResponse<T>(
        @Schema(description = "Contenido de la página", example = "[{\"id\":1,\"name\":\"Casa\",\"description\":\"Casa de campo\"}]")
        List<T> content,
        @Schema(description = "Número de la página actual", example = "0")
        int currentPage,
        @Schema(description = "Tamaño de la página", example = "10")
        int pageSize,
        @Schema(description = "Número total de páginas", example = "5")
        int totalPages,
        @Schema(description = "Número total de elementos", example = "50")
        long totalElements
) {
}
