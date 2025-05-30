package com.pragma.home360.home.application.dto.request.filters;

import io.swagger.v3.oas.annotations.media.Schema;

public record PropertyFilterRequest(
        @Schema(description = "Número de página (empieza desde 0)", example = "0", defaultValue = "0") Integer page,
        @Schema(description = "Cantidad de elementos por página", example = "20", defaultValue = "10") Integer size,
        @Schema(description = "Campo por el cual ordenar", example = "name", defaultValue = "id") String sortField,
        @Schema(description = "Dirección del ordenamiento", example = "DESC", defaultValue = "ASC", allowableValues = {"ASC", "DESC"}) String direction,
        @Schema(description = "Campo por el cual filtrar", example = "name", defaultValue = "id") String searchTerm) {

    public PropertyFilterRequest {
        if (page == null) {
            page = 0;
        }
        if (size == null) {
            size = 10;
        }
        if (sortField == null || sortField.isEmpty()) {
            sortField = "id";
        }
        if (direction == null || direction.isEmpty()) {
            direction = "ASC";
        }
        if (searchTerm == null || searchTerm.isEmpty()) {
            searchTerm = "";
        }
    }
}
