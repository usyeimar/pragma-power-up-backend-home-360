package com.pragma.home360.home.application.dto.request.filters;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;


@Schema(description = "Solicitud de paginación y ordenamiento con filtros básicos")
public record FilterRequest(
        @Schema(description = "Número de página (empieza desde 0)", example = "0", defaultValue = "0") Integer page,
        @Schema(description = "Cantidad de elementos por página (entre 1 y 100)", example = "20", defaultValue = "10") Integer size,
        @Schema(description = "Campo por el cual ordenar", example = "createdAt", defaultValue = "id") String sortField,
        @Schema(description = "Dirección del ordenamiento", example = "DESC", defaultValue = "ASC", allowableValues = {"ASC", "DESC"}) Sort.Direction direction,
        @Schema(description = "Término de búsqueda general", example = "producto") String searchTerm,
        @Schema(description = "Filtrar por estado activo/inactivo", example = "true") Boolean active,
        @Schema(description = "Fecha de inicio para filtrado (formato ISO)", example = "2023-01-01") @JsonFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
        @Schema(description = "Fecha de fin para filtrado (formato ISO)", example = "2023-12-31") @JsonFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

    public FilterRequest {
        if (page == null) page = 0;
        if (size == null) size = 10;
        if (sortField == null || sortField.isBlank()) sortField = "id";
        if (direction == null) direction = Sort.Direction.ASC;
    }

    public FilterRequest() {
        this(0, 10, "id", Sort.Direction.ASC, null, null, null, null);
    }
}