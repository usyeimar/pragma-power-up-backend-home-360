package com.pragma.home360.home.domain.utils.constants;

import java.util.Set;

public final class DomainConstants {


    private DomainConstants() {
    }

    /*
     *-----------------CATEGORY-----------------
     */
    public static final String CATEGORY_NAME_CANNOT_BE_EMPTY = "El nombre de la categoría no puede estar vacío.";
    public static final String CATEGORY_DESCRIPTION_CANNOT_BE_EMPTY = "La descripción de la categoría no puede estar vacía.";
    public static final String CATEGORY_NAME_MAX_LENGTH_EXCEEDED = "El nombre de la categoría no puede exceder los 50 caracteres.";
    public static final String CATEGORY_DESCRIPTION_MAX_LENGTH_EXCEEDED = "La descripción de la categoría no puede exceder los 90 caracteres.";
    public static final String CATEGORY_ALREADY_EXISTS_EXCEPTION_MESSAGE = "La categoría ya existe.";

    public static final int CATEGORY_NAME_MAX_LENGTH = 50;
    public static final int CATEGORY_DESCRIPTION_MAX_LENGTH = 90;

    /*
     *-----------------CITY/LOCATION-----------------
     */
    public static final String CITY_NAME_CANNOT_BE_EMPTY = "El nombre de la ciudad no puede estar vacío.";
    public static final String CITY_NAME_MAX_LENGTH_EXCEEDED = "El nombre de la ciudad no puede exceder los 50 caracteres.";
    public static final String CITY_DESCRIPTION_MAX_LENGTH_EXCEEDED = "La descripción de la ciudad no puede exceder los 255 caracteres.";
    public static final String CITY_NOT_FOUND = "City with id %s not found";

    public static final int CITY_NAME_MAX_LENGTH = 50;
    public static final int CITY_DESCRIPTION_MAX_LENGTH = 255;
    public static final String DEPARTMENT_ID_CANNOT_BE_NULL = "El ID del departamento no puede ser nulo.";
    public static final String CITY_NAME_ALREADY_EXISTS = "Ya existe una ciudad con el nombre %s.";

    /*
     *-----------------DEPARTMENT-----------------
     */
    public static final String DEPARTMENT_NAME_CANNOT_BE_EMPTY = "El nombre del departamento no puede estar vacío.";
    public static final String DEPARTMENT_DESCRIPTION_CANNOT_BE_EMPTY = "La descripción del departamento no puede estar vacía.";
    public static final String DEPARTMENT_NAME_MAX_LENGTH_EXCEEDED = "El nombre del departamento no puede exceder los %d caracteres.";
    public static final String DEPARTMENT_DESCRIPTION_MAX_LENGTH_EXCEEDED = "La descripción del departamento no puede exceder los %d caracteres.";
    public static final int DEPARTMENT_NAME_MAX_LENGTH = 50;
    public static final int DEPARTMENT_DESCRIPTION_MAX_LENGTH = 120;
    public static final String DEPARTMENT_NAME_ALREADY_EXISTS = "Ya existe un departamento con el nombre %s.";
    public static final String DEPARTMENT_NOT_FOUND = "No se encontró el departamento con ID %d.";


    /*
     *-----------------PAGINACIÓN / ORDENAMIENTO-----------------
     */
    public static final String PAGINATION_PAGE_NEGATIVE = "El número de página no puede ser negativo.";
    public static final String PAGINATION_SIZE_BETWEEN = "El tamaño de página debe estar entre 1 y 50.";
    public static final String PAGINATION_MAX_OFFSET = "page * size no puede exceed 10000 registros.";
    public static final Set<String> CATEGORY_ALLOWED_SORT_FIELDS = Set.of("id", "name", "createdAt");
    public static final String SORT_FIELD_INVALID = "sortField debe ser uno de: " + CATEGORY_ALLOWED_SORT_FIELDS + ".";
    public static final String SORT_DIRECTION_NULL = "La dirección de ordenamiento no puede ser nula.";
    public static final String LOCATION_SORT_DIRECTION_INVALID = "La dirección de ordenamiento debe ser 'ASC' o 'DESC'.";
    public static final String LOCATION_SORT_FIELD_INVALID = "El campo de ordenamiento debe ser 'city' o 'department'";
    public static final int INITIAL_PAGE = 0;
    public static final int MIN_PAGE_SIZE = 1;
    public static final int MAX_PAGE_SIZE = 50;
    public static final long MAX_PAGINATION_OFFSET = 10_000L;



    /*
     *-----------------NEIGHBORHOOD-----------------
     */
    public static final String NEIGHBORHOOD_NAME_CANNOT_BE_EMPTY = "El nombre del barrio no puede estar vacío.";
    public static final String NEIGHBORHOOD_DESCRIPTION_CANNOT_BE_EMPTY = "La descripción del barrio no puede estar vacía.";
    public static final String NEIGHBORHOOD_NAME_MAX_LENGTH_EXCEEDED = "El nombre del barrio no puede exceder los %d caracteres.";
    public static final String NEIGHBORHOOD_DESCRIPTION_MAX_LENGTH_EXCEEDED = "La descripción del barrio no puede exceder los %d caracteres.";
    public static final int NEIGHBORHOOD_NAME_MAX_LENGTH = 50;
    public static final int NEIGHBORHOOD_DESCRIPTION_MAX_LENGTH = 255;
    public static final String NEIGHBORHOOD_NAME_ALREADY_EXISTS = "Ya existe un barrio con el nombre %s.";
    public static final String NEIGHBORHOOD_NOT_FOUND = "No se encontró el barrio con ID %d.";
    public static final String NEIGHBORHOOD_INVALID_ZONE_TYPE = "Tipo de zona inválido. Valores permitidos: %s.";

    public static final String EXCEPTION_NOT_FOUND = "Clase de excepción no encontrada.";
}
