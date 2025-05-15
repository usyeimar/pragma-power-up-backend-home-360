package com.pragma.home360.home.domain.model;

import org.springframework.data.domain.Sort;

import java.time.LocalDate;

public record FilterModel(
        Integer page,
        Integer size,
        String sortField,
        Sort.Direction direction,
        String searchTerm,
        Boolean active,
        LocalDate startDate,
        LocalDate endDate
) {

    public FilterModel {
        if (page == null) page = 0;
        if (size == null) size = 10;
        if (sortField == null || sortField.isBlank()) sortField = "id";
        if (direction == null) direction = Sort.Direction.ASC;
    }

    public FilterModel() {
        this(0, 10, "id", Sort.Direction.ASC, null, null, null, null);
    }

    public BaseFilterModel toBaseFilter() {
        return new BaseFilterModel(page, size, sortField, direction);
    }
}