package com.pragma.user360.domain.model;

import org.springframework.data.domain.Sort;

public record UserFilterModel(
        Integer page,
        Integer size,
        String sortField,
        Sort.Direction direction
) {

    public UserFilterModel {
        page = (page == null) ? 0 : page;
        size = (size == null) ? 10 : size;
        sortField = (sortField == null) ? "id" : sortField;
        direction = (direction == null) ? Sort.Direction.ASC : direction;
    }
}