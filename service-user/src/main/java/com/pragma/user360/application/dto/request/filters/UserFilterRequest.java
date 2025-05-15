package com.pragma.user360.application.dto.request.filters;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Sort;

public record UserFilterRequest(
        @Schema(description = "Número de página (empieza desde 0)", example = "0", defaultValue = "0")
        Integer page,
        @Schema(description = "Cantidad de elementos por página", example = "20", defaultValue = "10")
        Integer size,
        String sortField,
        @Schema(description = "Dirección del ordenamiento", example = "DESC", defaultValue = "ASC", allowableValues = {"ASC", "DESC"})
        Sort.Direction direction

) {

    public UserFilterRequest {
        page = (page == null) ? 0 : page;
        size = (size == null) ? 10 : size;
        sortField = (sortField == null) ? "id" : sortField;

        //if direction is different from ASC or DESC, set it to ASC
        if (direction != null && !direction.isAscending() && !direction.isDescending()) {
            direction = Sort.Direction.ASC;
        }

        direction = (direction == null) ? Sort.Direction.ASC : direction;
    }
}
