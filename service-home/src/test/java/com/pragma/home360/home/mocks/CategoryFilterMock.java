package com.pragma.home360.home.mocks;

import com.pragma.home360.home.domain.model.CategoryFilterModel;
import org.springframework.data.domain.Sort;

/**
 * Clase de utilidad para crear instancias de CategoryFilterModel para pruebas
 */
public class CategoryFilterMock {

    /**
     * Crea un objeto de filtro de categoría con los parámetros especificados
     *
     * @param page Número de página
     * @param size Tamaño de página
     * @param sortField Campo por el cual ordenar
     * @param direction Dirección de ordenamiento
     * @return Una instancia de CategoryFilterModel con los valores proporcionados
     */
    public static CategoryFilterModel createCategoryFilter(
            Integer page,
            Integer size,
            String sortField,
            Sort.Direction direction) {
        return new CategoryFilterModel(page, size, sortField, direction);
    }

    /**
     * Crea un filtro con valores predeterminados válidos
     *
     * @return Una instancia de CategoryFilterModel con valores predeterminados
     */
    public static CategoryFilterModel createDefaultCategoryFilter() {
        return createCategoryFilter(0, 10, "id", Sort.Direction.ASC);
    }

    /**
     * Crea un filtro con un número de página específico
     *
     * @param page Número de página
     * @return Una instancia de CategoryFilterModel con el número de página especificado
     */
    public static CategoryFilterModel createCategoryFilterWithPage(Integer page) {
        return createCategoryFilter(page, 10, "id", Sort.Direction.ASC);
    }

    /**
     * Crea un filtro con un tamaño de página específico
     *
     * @param size Tamaño de página
     * @return Una instancia de CategoryFilterModel con el tamaño de página especificado
     */
    public static CategoryFilterModel createCategoryFilterWithSize(Integer size) {
        return createCategoryFilter(0, size, "id", Sort.Direction.ASC);
    }

    /**
     * Crea un filtro con un campo de ordenamiento específico
     *
     * @param sortField Campo de ordenamiento
     * @return Una instancia de CategoryFilterModel con el campo de ordenamiento especificado
     */
    public static CategoryFilterModel createCategoryFilterWithSortField(String sortField) {
        return createCategoryFilter(0, 10, sortField, Sort.Direction.ASC);
    }

    /**
     * Crea un filtro con una dirección de ordenamiento específica
     *
     * @param direction Dirección de ordenamiento
     * @return Una instancia de CategoryFilterModel con la dirección de ordenamiento especificada
     */
    public static CategoryFilterModel createCategoryFilterWithDirection(Sort.Direction direction) {
        return createCategoryFilter(0, 10, "id", direction);
    }

    /**
     * Crea un filtro con un offset que excede el máximo permitido
     *
     * @return Una instancia de CategoryFilterModel con offset que excede el límite
     */
    public static CategoryFilterModel createCategoryFilterWithExceededOffset() {
        return createCategoryFilter(201, 50, "id", Sort.Direction.ASC);
    }

    /**
     * Crea un filtro con página negativa para pruebas de validación
     *
     * @return Una instancia de CategoryFilterModel con página negativa
     */
    public static CategoryFilterModel createCategoryFilterWithNegativePage() {
        return createCategoryFilter(-1, 10, "id", Sort.Direction.ASC);
    }

    /**
     * Crea un filtro con tamaño de página inválido para pruebas de validación
     *
     * @param invalidSize Tamaño de página inválido (0, negativo o mayor que el límite)
     * @return Una instancia de CategoryFilterModel con tamaño inválido
     */
    public static CategoryFilterModel createCategoryFilterWithInvalidSize(Integer invalidSize) {
        return createCategoryFilter(0, invalidSize, "id", Sort.Direction.ASC);
    }

    /**
     * Crea un filtro con campo de ordenamiento inválido para pruebas de validación
     *
     * @return Una instancia de CategoryFilterModel con campo de ordenamiento inválido
     */
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