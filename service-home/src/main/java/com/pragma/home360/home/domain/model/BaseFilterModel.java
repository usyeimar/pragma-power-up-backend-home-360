package com.pragma.home360.home.domain.model;

import org.springframework.data.domain.Sort;

public record BaseFilterModel(
    Integer page,
    Integer size,
    String sortField,
    Sort.Direction direction
) {
    public BaseFilterModel {
        if (page == null) page = 0;
        if (size == null) size = 10;
        if (sortField == null || sortField.isBlank()) sortField = "id";
        if (direction == null) direction = Sort.Direction.ASC;
    }

    public BaseFilterModel() {
        this(0, 10, "id", Sort.Direction.ASC);
    }
} 