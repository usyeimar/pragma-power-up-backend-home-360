package com.pragma.home360.home.unit.mocks;

import com.pragma.home360.home.domain.model.CategoryFilterModel;
import org.springframework.data.domain.Sort;


public class CategoryFilterMock {


    public static CategoryFilterModel createCategoryFilter(
            Integer page,
            Integer size,
            String sortField,
            Sort.Direction direction) {
        return new CategoryFilterModel(page, size, sortField, direction);
    }


    public static CategoryFilterModel createDefaultCategoryFilter() {
        return createCategoryFilter(0, 10, "id", Sort.Direction.ASC);
    }


    public static CategoryFilterModel createCategoryFilterWithPage(Integer page) {
        return createCategoryFilter(page, 10, "id", Sort.Direction.ASC);
    }


    public static CategoryFilterModel createCategoryFilterWithSize(Integer size) {
        return createCategoryFilter(0, size, "id", Sort.Direction.ASC);
    }


    public static CategoryFilterModel createCategoryFilterWithSortField(String sortField) {
        return createCategoryFilter(0, 10, sortField, Sort.Direction.ASC);
    }


    public static CategoryFilterModel createCategoryFilterWithDirection(Sort.Direction direction) {
        return createCategoryFilter(0, 10, "id", direction);
    }


    public static CategoryFilterModel createCategoryFilterWithExceededOffset() {
        return createCategoryFilter(201, 50, "id", Sort.Direction.ASC);
    }


    public static CategoryFilterModel createCategoryFilterWithNegativePage() {
        return createCategoryFilter(-1, 10, "id", Sort.Direction.ASC);
    }


    public static CategoryFilterModel createCategoryFilterWithInvalidSize(Integer invalidSize) {
        return createCategoryFilter(0, invalidSize, "id", Sort.Direction.ASC);
    }


    public static CategoryFilterModel createCategoryFilterWithInvalidSortField() {
        return createCategoryFilter(0, 10, "invalidField", Sort.Direction.ASC);
    }

    /**
     * Crea un filtro con dirección nula para pruebas de validación
     *
     * @return Una instancia de CategoryFilterModel con dirección nula
     */
    public static CategoryFilterModel createCategoryFilterWithNullDirection() {
        return createCategoryFilter(0, 10, "id", null);
    }
}