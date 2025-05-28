package com.pragma.home360.home.domain.utils.constants;

import java.util.Set;

public final class DomainConstants {

    private DomainConstants() {
    }

    public static final String RESOURCE_NOT_FOUND_BY_ID = "%s no encontrado(a) con ID: %s";
    public static final String RESOURCE_ALREADY_EXISTS_WITH_NAME = "Ya existe un(a) %s con el nombre '%s'.";
    public static final String FIELD_CANNOT_BE_EMPTY = "El campo '%s' no puede estar vacío.";
    public static final String FIELD_CANNOT_BE_NULL = "El campo '%s' no puede ser nulo.";
    public static final String FIELD_MAX_LENGTH_EXCEEDED = "El campo '%s' no puede exceder los %d caracteres.";
    public static final String FIELD_MUST_BE_POSITIVE = "El campo '%s' debe ser un valor positivo.";

    public static final String CATEGORY_NAME_CANNOT_BE_EMPTY = "El nombre de la categoría no puede estar vacío.";
    public static final String CATEGORY_DESCRIPTION_CANNOT_BE_EMPTY = "La descripción de la categoría no puede estar vacía.";
    public static final String CATEGORY_NAME_MAX_LENGTH_EXCEEDED = "El nombre de la categoría no puede exceder los 50 caracteres.";
    public static final String CATEGORY_DESCRIPTION_MAX_LENGTH_EXCEEDED = "La descripción de la categoría no puede exceder los 90 caracteres.";
    public static final String CATEGORY_ALREADY_EXISTS_EXCEPTION_MESSAGE = "La categoría ya existe.";
    public static final String CATEGORY_NOT_FOUND_BY_ID = "Categoría no encontrada con ID: %s";

    public static final int CATEGORY_NAME_MAX_LENGTH = 50;
    public static final int CATEGORY_DESCRIPTION_MAX_LENGTH = 90;

    public static final String CITY_NAME_CANNOT_BE_EMPTY = "El nombre de la ciudad no puede estar vacío.";
    public static final String CITY_NAME_MAX_LENGTH_EXCEEDED = "El nombre de la ciudad no puede exceder los 50 caracteres.";
    public static final String CITY_DESCRIPTION_MAX_LENGTH_EXCEEDED = "La descripción de la ciudad no puede exceder los 255 caracteres.";
    public static final String CITY_NOT_FOUND = "Ciudad no encontrada con ID: %s";
    public static final String CITY_NAME_ALREADY_EXISTS = "Ya existe una ciudad con el nombre %s.";
    public static final int CITY_NAME_MAX_LENGTH = 50;
    public static final int CITY_DESCRIPTION_MAX_LENGTH = 255;
    public static final String DEPARTMENT_ID_CANNOT_BE_NULL = "El ID del departamento no puede ser nulo.";

    public static final String DEPARTMENT_NAME_CANNOT_BE_EMPTY = "El nombre del departamento no puede estar vacío.";
    public static final String DEPARTMENT_DESCRIPTION_CANNOT_BE_EMPTY = "La descripción del departamento no puede estar vacía.";
    public static final String DEPARTMENT_NAME_MAX_LENGTH_EXCEEDED = "El nombre del departamento no puede exceder los %d caracteres.";
    public static final String DEPARTMENT_DESCRIPTION_MAX_LENGTH_EXCEEDED = "La descripción del departamento no puede exceder los %d caracteres.";
    public static final int DEPARTMENT_NAME_MAX_LENGTH = 50;
    public static final int DEPARTMENT_DESCRIPTION_MAX_LENGTH = 120;
    public static final String DEPARTMENT_NAME_ALREADY_EXISTS = "Ya existe un departamento con el nombre %s.";
    public static final String DEPARTMENT_NOT_FOUND = "Departamento no encontrado con ID: %s.";

    public static final String PAGINATION_PAGE_NEGATIVE = "El número de página no puede ser negativo.";
    public static final String PAGINATION_SIZE_BETWEEN = "El tamaño de página debe estar entre 1 y 50.";
    public static final String PAGINATION_MAX_OFFSET = "page * size no puede exceder 10000 registros.";
    public static final Set<String> CATEGORY_ALLOWED_SORT_FIELDS = Set.of("id", "name", "createdAt");
    public static final String SORT_FIELD_INVALID = "sortField debe ser uno de: " + CATEGORY_ALLOWED_SORT_FIELDS + ".";
    public static final String SORT_DIRECTION_NULL = "La dirección de ordenamiento no puede ser nula.";
    public static final String LOCATION_SORT_DIRECTION_INVALID = "La dirección de ordenamiento debe ser 'ASC' o 'DESC'.";
    public static final String LOCATION_SORT_FIELD_INVALID = "El campo de ordenamiento debe ser 'city' o 'department'";
    public static final int INITIAL_PAGE = 0;
    public static final int MIN_PAGE_SIZE = 1;
    public static final int MAX_PAGE_SIZE = 50;
    public static final long MAX_PAGINATION_OFFSET = 10_000L;

    public static final String NEIGHBORHOOD_NAME_CANNOT_BE_EMPTY = "El nombre del barrio no puede estar vacío.";
    public static final String NEIGHBORHOOD_DESCRIPTION_CANNOT_BE_EMPTY = "La descripción del barrio no puede estar vacía.";
    public static final String NEIGHBORHOOD_NAME_MAX_LENGTH_EXCEEDED = "El nombre del barrio no puede exceder los %d caracteres.";
    public static final String NEIGHBORHOOD_DESCRIPTION_MAX_LENGTH_EXCEEDED = "La descripción del barrio no puede exceder los %d caracteres.";
    public static final int NEIGHBORHOOD_NAME_MAX_LENGTH = 50;
    public static final int NEIGHBORHOOD_DESCRIPTION_MAX_LENGTH = 255;
    public static final String NEIGHBORHOOD_NAME_ALREADY_EXISTS = "Ya existe un barrio con el nombre %s.";
    public static final String NEIGHBORHOOD_NOT_FOUND = "Barrio no encontrado con ID: %s.";
    public static final String NEIGHBORHOOD_INVALID_ZONE_TYPE = "Tipo de zona inválido. Valores permitidos: %s.";

    public static final String PROPERTY_NOT_FOUND_BY_ID = "Propiedad no encontrada con ID: %s.";
    public static final String PROPERTY_NAME_CANNOT_BE_EMPTY = "El nombre de la propiedad no puede estar vacío.";
    public static final String PROPERTY_NAME_MAX_LENGTH_EXCEEDED = "El nombre de la propiedad no puede exceder los %d caracteres.";
    public static final int PROPERTY_NAME_MAX_LENGTH = 255;
    public static final String PROPERTY_PRICE_MUST_BE_POSITIVE = "El precio debe ser un valor positivo.";
    public static final String PROPERTY_ROOMS_MUST_BE_POSITIVE = "El número de habitaciones debe ser positivo.";
    public static final String PROPERTY_BATHROOMS_MUST_BE_POSITIVE = "El número de baños debe ser positivo.";
    public static final String PROPERTY_ACTIVE_DATE_REQUIRED = "La fecha de publicación activa es obligatoria.";
    public static final String PROPERTY_ACTIVE_DATE_IN_PAST = "La fecha de publicación activa no puede ser en el pasado.";
    public static final String PROPERTY_ACTIVE_DATE_EXCEEDS_LIMIT = "La fecha de publicación activa no puede exceder un mes desde la fecha actual.";
    public static final String PROPERTY_CATEGORY_ID_REQUIRED = "El ID de la categoría es obligatorio para la propiedad.";
    public static final String PROPERTY_LOCATION_ID_REQUIRED = "El ID de la ubicación es obligatorio para la propiedad.";
    public static final String PROPERTY_NOT_FOUND_FOR_UPDATE = "Propiedad no encontrada con ID: %s para actualizar.";
    public static final String PROPERTY_CANNOT_CHANGE_ACTIVE_DATE_TO_PAST = "No se puede cambiar la fecha de publicación activa a una fecha pasada para una propiedad ya publicada.";
    public static final String PROPERTY_NOT_FOUND_FOR_DELETE = "Propiedad no encontrada con ID: %s para eliminar.";
    public static final String PROPERTY_LOG_PROCESSING_PENDING = "Procesando propiedades pendientes de publicación para la fecha: {}";
    public static final String PROPERTY_LOG_NO_PENDING_TO_UPDATE = "No hay propiedades pendientes para actualizar a PUBLICADA hoy.";
    public static final String PROPERTY_LOG_FOUND_TO_UPDATE = "Se encontraron {} propiedades para actualizar a PUBLICADA.";
    public static final String PROPERTY_LOG_UPDATING_TO_PUBLISHED = "Actualizando propiedad ID: {} de PENDING a PUBLISHED.";
    public static final String PROPERTY_LOG_UPDATED_COUNT = "Se actualizaron {} propiedades a PUBLICADA.";
    public static final String LOCATION_NOT_FOUND_BY_ID = "Ubicación no encontrada con ID: %s";

    public static final String PROPERTY_IMAGE_PROPERTY_NOT_FOUND = "Propiedad no encontrada con ID: %s para asociar imagen.";
    public static final String PROPERTY_IMAGE_FILE_EMPTY = "El archivo de imagen no puede ser nulo o vacío.";
    public static final String PROPERTY_IMAGE_STORAGE_FAILED = "Falló al guardar el archivo de imagen: %s";
    public static final String PROPERTY_IMAGE_NOT_FOUND_BY_ID = "Imagen de propiedad no encontrada con ID: %s";
    public static final String PROPERTY_IMAGE_NOT_FOUND_FOR_DELETE = "Imagen de propiedad no encontrada con ID: %s para eliminar.";
    public static final String PROPERTY_IMAGE_LOG_DELETE_FAILED = "Falló al eliminar el archivo de imagen del almacenamiento: %s - %s";
    public static final String PROPERTY_IMAGE_DOES_NOT_BELONG_TO_PROPERTY = "La imagen con ID %s no pertenece a la propiedad con ID %s";

    public static final String SCHEDULED_TASK_PROPERTY_UPDATE_STARTED = "Iniciando tarea programada: Actualizar propiedades pendientes a publicadas.";
    public static final String SCHEDULED_TASK_PROPERTY_UPDATE_SUCCESS = "Tarea programada: Actualización de propiedades pendientes finalizada exitosamente.";
    public static final String SCHEDULED_TASK_PROPERTY_UPDATE_ERROR = "Error durante la ejecución de la tarea programada para actualizar propiedades: {}";
    public static final String EXCEPTION_NOT_FOUND = "Clase de excepción no encontrada.";

    // Constantes para mensajes internos de Validator.java
    public static final String VALIDATOR_CUSTOM_EXCEPTION_NOT_RUNTIME = "La clase de excepción personalizada '%s' no extiende RuntimeException.";
    public static final String VALIDATOR_CUSTOM_EXCEPTION_CLASS_NOT_FOUND = "Clase de excepción personalizada no encontrada: %s";
    public static final String VALIDATOR_CUSTOM_EXCEPTION_CONSTRUCTOR_NOT_FOUND = "Constructor (String) no encontrado para la clase de excepción personalizada: %s";
    public static final String VALIDATOR_CUSTOM_EXCEPTION_INSTANTIATION_FAILED = "No se pudo instanciar la clase de excepción personalizada (puede ser abstracta o interfaz): %s";
    public static final String VALIDATOR_CUSTOM_EXCEPTION_ILLEGAL_ACCESS = "Acceso ilegal al constructor de la clase de excepción personalizada: %s";
    public static final String VALIDATOR_CUSTOM_EXCEPTION_CONSTRUCTOR_THREW_CHECKED = "El constructor de la excepción personalizada '%s' lanzó una excepción chequeada: %s";
    public static final String VALIDATOR_CUSTOM_EXCEPTION_INVOCATION_TARGET_NO_CAUSE = "InvocationTargetException sin causa al llamar al constructor de '%s'.";
    public static final String VALIDATOR_WARN_MESSAGE_FORMATTING_FAILED = "ADVERTENCIA: Falló la formatación del mensaje de error. Plantilla: '%s', Valor: '%s'. Error: %s";
}