package com.pragma.home360.home.unit.mocks;

import com.pragma.home360.home.domain.model.FilterModel;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

/**
 * Clase de utilidad para crear instancias de FilterModel para pruebas
 */
public class FilterRequestMock {

    /**
     * Crea un objeto de filtro con los parámetros especificados
     *
     * @param page       Número de página
     * @param size       Tamaño de página
     * @param sortField  Campo por el cual ordenar
     * @param direction  Dirección de ordenamiento
     * @param searchTerm Término de búsqueda
     * @param active     Estado activo
     * @param startDate  Fecha de inicio
     * @param endDate    Fecha de fin
     * @return Una instancia de FilterModel con los valores proporcionados
     */
    public static FilterModel createFilterRequest(Integer page, Integer size, String sortField, Sort.Direction direction, String searchTerm, Boolean active, LocalDate startDate, LocalDate endDate) {
        return new FilterModel(page, size, sortField, direction, searchTerm, active, startDate, endDate);
    }

    /**
     * Crea un objeto de filtro con los parámetros básicos especificados y el resto predeterminados
     *
     * @param page      Número de página
     * @param size      Tamaño de página
     * @param sortField Campo por el cual ordenar
     * @param direction Dirección de ordenamiento
     * @return Una instancia de FilterModel con los valores proporcionados
     */
    public static FilterModel createFilterRequest(Integer page, Integer size, String sortField, Sort.Direction direction) {
        return new FilterModel(page, size, sortField, direction, null, null, null, null);
    }

    /**
     * Crea un filtro con valores predeterminados válidos
     *
     * @return Una instancia de FilterModel con valores predeterminados
     */
    public static FilterModel createDefaultFilterRequest() {
        return createFilterRequest(0, 10, "id", Sort.Direction.ASC);
    }

    /**
     * Crea un filtro con un número de página específico
     *
     * @param page Número de página
     * @return Una instancia de FilterModel con el número de página especificado
     */
    public static FilterModel createFilterRequestWithPage(Integer page) {
        return createFilterRequest(page, 10, "id", Sort.Direction.ASC);
    }

    /**
     * Crea un filtro con un tamaño de página específico
     *
     * @param size Tamaño de página
     * @return Una instancia de FilterModel con el tamaño de página especificado
     */
    public static FilterModel createFilterRequestWithSize(Integer size) {
        return createFilterRequest(0, size, "id", Sort.Direction.ASC);
    }

    /**
     * Crea un filtro con un campo de ordenamiento específico
     *
     * @param sortField Campo de ordenamiento
     * @return Una instancia de FilterModel con el campo de ordenamiento especificado
     */
    public static FilterModel createFilterRequestWithSortField(String sortField) {
        return createFilterRequest(0, 10, sortField, Sort.Direction.ASC);
    }

    /**
     * Crea un filtro con una dirección de ordenamiento específica
     *
     * @param direction Dirección de ordenamiento
     * @return Una instancia de FilterModel con la dirección de ordenamiento especificada
     */
    public static FilterModel createFilterRequestWithDirection(Sort.Direction direction) {
        return createFilterRequest(0, 10, "id", direction);
    }

    /**
     * Crea un filtro con un término de búsqueda específico
     *
     * @param searchTerm Término de búsqueda
     * @return Una instancia de FilterModel con el término de búsqueda especificado
     */
    public static FilterModel createFilterRequestWithSearchTerm(String searchTerm) {
        return createFilterRequest(0, 10, "id", Sort.Direction.ASC, searchTerm, null, null, null);
    }

    /**
     * Crea un filtro con estado activo específico
     *
     * @param active Estado activo
     * @return Una instancia de FilterModel con el estado activo especificado
     */
    public static FilterModel createFilterRequestWithActive(Boolean active) {
        return createFilterRequest(0, 10, "id", Sort.Direction.ASC, null, active, null, null);
    }

    /**
     * Crea un filtro con fechas específicas
     *
     * @param startDate Fecha de inicio
     * @param endDate   Fecha de fin
     * @return Una instancia de FilterModel con las fechas especificadas
     */
    public static FilterModel createFilterRequestWithDates(LocalDate startDate, LocalDate endDate) {
        return createFilterRequest(0, 10, "id", Sort.Direction.ASC, null, null, startDate, endDate);
    }

    /**
     * Crea un filtro con un offset que excede el máximo permitido
     *
     * @return Una instancia de FilterModel con offset que excede el límite
     */
    public static FilterModel createFilterRequestWithExceededOffset() {
        return createFilterRequest(201, 50, "id", Sort.Direction.ASC);
    }

    /**
     * Crea un filtro con página negativa para pruebas de validación
     *
     * @return Una instancia de FilterModel con página negativa
     */
    public static FilterModel createFilterRequestWithNegativePage() {
        return createFilterRequest(-1, 10, "id", Sort.Direction.ASC);
    }

    /**
     * Crea un filtro con tamaño de página inválido para pruebas de validación
     *
     * @param invalidSize Tamaño de página inválido (0, negativo o mayor que el límite)
     * @return Una instancia de FilterModel con tamaño inválido
     */
    public static FilterModel createFilterRequestWithInvalidSize(Integer invalidSize) {
        return createFilterRequest(0, invalidSize, "id", Sort.Direction.ASC);
    }

    /**
     * Crea un filtro con campo de ordenamiento inválido para pruebas de validación
     *
     * @return Una instancia de FilterModel con campo de ordenamiento inválido
     */
    public static FilterModel createFilterRequestWithInvalidSortField() {
        return createFilterRequest(0, 10, "invalidField", Sort.Direction.ASC);
    }

    /**
     * Crea un filtro con dirección nula para pruebas de validación
     *
     * @return Una instancia de FilterModel con dirección nula
     */
    public static FilterModel createFilterRequestWithNullDirection() {
        return createFilterRequest(0, 10, "id", null);
    }
}