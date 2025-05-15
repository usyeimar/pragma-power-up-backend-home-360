package com.pragma.home360.home.application.dto.request.filters;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Sort;

@Schema(description = "Filtro para consulta de categorías con paginación")
public record CategoryFilterRequest(
        @Schema(description = "Número de página (empieza desde 0)", example = "0", defaultValue = "0") Integer page,
        @Schema(description = "Cantidad de elementos por página", example = "20", defaultValue = "10") Integer size,
        @Schema(description = "Campo por el cual ordenar", example = "name", defaultValue = "id") String sortField,
        @Schema(description = "Dirección del ordenamiento", example = "DESC", defaultValue = "ASC", allowableValues = {"ASC", "DESC"}) Sort.Direction direction) {

    public CategoryFilterRequest {
        page = (page == null) ? 0 : page;
        size = (size == null) ? 10 : size;
        sortField = (sortField == null) ? "id" : sortField;
        direction = (direction == null) ? Sort.Direction.ASC : direction;
    }
}