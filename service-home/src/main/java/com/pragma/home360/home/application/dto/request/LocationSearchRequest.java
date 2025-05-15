package com.pragma.home360.home.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Sort;

@Schema(description = "Solicitud para búsqueda de ubicaciones")
public record LocationSearchRequest(
        @Schema(description = "Texto de búsqueda (busca en ciudad o departamento)", example = "Medellin") String searchText,
        @Schema(description = "Número de página (empieza desde 0)", example = "0", defaultValue = "0") Integer page,
        @Schema(description = "Cantidad de elementos por página", example = "10", defaultValue = "10") Integer size,
        @Schema(description = "Campo por el cual ordenar (city o department)", example = "city", defaultValue = "city") String sortBy,
        @Schema(description = "Dirección del ordenamiento", example = "ASC", defaultValue = "ASC", allowableValues = {"ASC", "DESC"}) Sort.Direction direction) {

    public LocationSearchRequest {
        page = (page == null) ? 0 : page;
        size = (size == null) ? 10 : size;
        sortBy = (sortBy == null || sortBy.isBlank()) ? "city" : sortBy;
        direction = (direction == null) ? Sort.Direction.ASC : direction;
    }

    public LocationSearchRequest() {
        this(null, 0, 10, "city", Sort.Direction.ASC);
    }


}