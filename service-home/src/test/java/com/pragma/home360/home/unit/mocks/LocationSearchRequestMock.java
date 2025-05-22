package com.pragma.home360.home.unit.mocks;

import com.pragma.home360.home.application.dto.request.LocationSearchRequest;
import org.springframework.data.domain.Sort;

/**
 * Clase de utilidad para crear instancias de LocationSearchRequest para pruebas
 */
public class LocationSearchRequestMock {

    /**
     * Crea un objeto de búsqueda de ubicación con los parámetros especificados
     *
     * @param searchText Texto de búsqueda
     * @param page       Número de página
     * @param size       Tamaño de página
     * @param sortBy     Campo por el cual ordenar
     * @param direction  Dirección de ordenamiento
     * @return Una instancia de LocationSearchRequest con los valores proporcionados
     */
    public static LocationSearchRequest createLocationSearchRequest(
            String searchText, Integer page, Integer size, String sortBy, Sort.Direction direction) {
        return new LocationSearchRequest(searchText, page, size, sortBy, direction);
    }

    /**
     * Crea una búsqueda de ubicación con valores predeterminados válidos
     *
     * @return Una instancia de LocationSearchRequest con valores predeterminados
     */
    public static LocationSearchRequest createDefaultLocationSearchRequest() {
        return createLocationSearchRequest(null, 0, 10, "city", Sort.Direction.ASC);
    }

    /**
     * Crea una búsqueda de ubicación con un texto de búsqueda específico
     *
     * @param searchText Texto de búsqueda
     * @return Una instancia de LocationSearchRequest con el texto de búsqueda especificado
     */
    public static LocationSearchRequest createLocationSearchRequestWithSearchText(String searchText) {
        return createLocationSearchRequest(searchText, 0, 10, "city", Sort.Direction.ASC);
    }

    /**
     * Crea una búsqueda de ubicación con un número de página específico
     *
     * @param page Número de página
     * @return Una instancia de LocationSearchRequest con el número de página especificado
     */
    public static LocationSearchRequest createLocationSearchRequestWithPage(Integer page) {
        return createLocationSearchRequest(null, page, 10, "city", Sort.Direction.ASC);
    }

    /**
     * Crea una búsqueda de ubicación con un tamaño de página específico
     *
     * @param size Tamaño de página
     * @return Una instancia de LocationSearchRequest con el tamaño de página especificado
     */
    public static LocationSearchRequest createLocationSearchRequestWithSize(Integer size) {
        return createLocationSearchRequest(null, 0, size, "city", Sort.Direction.ASC);
    }

    /**
     * Crea una búsqueda de ubicación con un campo de ordenamiento específico
     *
     * @param sortBy Campo de ordenamiento
     * @return Una instancia de LocationSearchRequest con el campo de ordenamiento especificado
     */
    public static LocationSearchRequest createLocationSearchRequestWithSortBy(String sortBy) {
        return createLocationSearchRequest(null, 0, 10, sortBy, Sort.Direction.ASC);
    }
}