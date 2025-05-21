package com.pragma.home360.home.infrastructure.exceptionshandler;

public enum ApiErrorCode {
    // General Errors (000-099)
    GENERIC_ERROR("ERR000", "Ocurrió un error inesperado."),
    UNAUTHORIZED("ERR001", "No autorizado para realizar esta acción."),
    ACCESS_DENIED("ERR002", "Acceso denegado."),
    SERVICE_UNAVAILABLE("ERR003", "Servicio no disponible temporalmente."),

    // Validation Errors (100-199)
    VALIDATION_ERROR("ERR100", "Error de validación en los parámetros de entrada."),
    INVALID_PARAMETER_FORMAT("ERR101", "El formato del parámetro '%s' es inválido."),
    MISSING_REQUIRED_PARAMETER("ERR102", "El parámetro requerido '%s' no fue proporcionado."),
    INVALID_ENUM_VALUE("ERR103", "Valor inválido para el campo '%s'. Valores esperados: %s."),

    // Resource Errors (200-299)
    RESOURCE_NOT_FOUND("ERR200", "Recurso no encontrado."),
    MODEL_NOT_FOUND("ERR201", "%s no encontrado(a) con ID: %s."), // Example: "Categoría no encontrada con ID: 5"
    ALREADY_EXISTS("ERR202", "El recurso ya existe."),
    CATEGORY_ALREADY_EXISTS("ERR203", "La categoría con nombre '%s' ya existe."),
    DEPARTMENT_ALREADY_EXISTS("ERR204", "El departamento con nombre '%s' ya existe."),
    CITY_ALREADY_EXISTS("ERR205", "La ciudad con nombre '%s' ya existe."),
    NEIGHBORHOOD_ALREADY_EXISTS("ERR206", "El barrio con nombre '%s' ya existe en la ciudad especificada."),
    PROPERTY_ALREADY_EXISTS("ERR207", "La propiedad con nombre '%s' ya existe."),


    // Property Specific Errors (300-399)
    PROPERTY_ACTIVE_DATE_INVALID("ERR300", "La fecha de publicación activa de la propiedad es inválida."),
    PROPERTY_IMAGE_UPLOAD_FAILED("ERR301", "Falló la carga de la imagen de la propiedad."),
    PROPERTY_IMAGE_NOT_FOUND("ERR302", "Imagen de propiedad no encontrada."),
    PROPERTY_IMAGE_MAX_COUNT_EXCEEDED("ERR303", "Se ha excedido el número máximo de imágenes para la propiedad."),


    // File Storage Errors (400-499)
    FILE_STORAGE_ERROR("ERR400", "Error durante la operación de almacenamiento de archivos."),
    FILE_NOT_FOUND("ERR401", "Archivo no encontrado en el almacenamiento."),
    FILE_DELETION_FAILED("ERR402", "No se pudo eliminar el archivo del almacenamiento."),

    // Database/Persistence Errors (500-599)
    DATABASE_ERROR("ERR500", "Error al interactuar con la base de datos."),
    CONSTRAINT_VIOLATION("ERR501", "Violación de una restricción de la base de datos.");


    private final String code;
    private final String defaultMessage;

    ApiErrorCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public String getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    /**
     * Returns a formatted message. If params are provided, it tries to format the defaultMessage.
     *
     * @param params Arguments referenced by the format specifiers in the format string.
     * @return The formatted message or the default message if no params or formatting error.
     */
    public String getFormattedMessage(Object... params) {
        if (params == null || params.length == 0) {
            return defaultMessage;
        }
        try {
            return String.format(defaultMessage, params);
        } catch (Exception e) {
            return defaultMessage;
        }
    }
}
