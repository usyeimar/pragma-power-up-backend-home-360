package com.pragma.home360.home.unit.mocks;

import com.pragma.home360.home.domain.model.PropertyFilterModel;

public class PropertyFilterMock {

    public static PropertyFilterModel createPropertyFilter(Integer page, Integer size, String sortField, String direction, String searchTerm) {
        return new PropertyFilterModel(page, size, sortField, direction, searchTerm);
    }

    public static PropertyFilterModel createDefaultPropertyFilter() {
        return createPropertyFilter(0, 10, "id", "ASC", null);
    }

    public static PropertyFilterModel createPropertyFilterWithPage(Integer page) {
        return createPropertyFilter(page, 10, "id", "ASC", null);
    }

    public static PropertyFilterModel createPropertyFilterWithSize(Integer size) {
        return createPropertyFilter(0, size, "id", "ASC", null);
    }

    public static PropertyFilterModel createPropertyFilterWithNegativePage() {
        return createPropertyFilter(-1, 10, "id", "ASC", null);
    }

    public static PropertyFilterModel createPropertyFilterWithInvalidSize(Integer invalidSize) {
        return createPropertyFilter(0, invalidSize, "id", "ASC", null);
    }

    public static PropertyFilterModel createPropertyFilterWithExceededOffset() {
        // Assuming MAX_PAGINATION_OFFSET = 10_000L, MAX_PAGE_SIZE = 50
        // page * size > 10000. (201 * 50 = 10050)
        return createPropertyFilter(201, 50, "id", "ASC", null);
    }

    public static PropertyFilterModel createPropertyFilterWithSearchTerm(String searchTerm) {
        return createPropertyFilter(0, 10, "id", "ASC", searchTerm);
    }
}