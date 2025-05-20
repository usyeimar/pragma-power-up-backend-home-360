package com.pragma.home360.home.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;

public record PropertyFilterModel(
        Integer page,
        Integer size,
        String sortField,
        String direction,
        String searchTerm
) {
}
