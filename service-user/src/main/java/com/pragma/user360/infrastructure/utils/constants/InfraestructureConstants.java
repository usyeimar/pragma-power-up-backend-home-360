package com.pragma.user360.infrastructure.utils.constants;

public class InfraestructureConstants {


    /*
     *-----------------CATEGORY-----------------
     */
    public static final String PAGEABLE_FIELD_NAME = "name";


    public static final String ERROR_VALIDATION_TITLE = "Error de validación en los parámetros";
    public static final String ERROR_TYPE_MISMATCH_TITLE = "Error de tipo en el parámetro: %s";
    public static final String ERROR_VALIDATION_GENERIC_TITLE = "Error de validación";
    public static final String ERROR_NULL_REFERENCE_TITLE = "Error: referencia nula";
    public static final String ERROR_ALREADY_EXISTS_TITLE = "Error: el recurso ya existe";
    public static final String ERROR_DESCRIPTION_TOO_LONG_TITLE = "Error: descripción demasiado larga";
    public static final String ERROR_NAME_TOO_LONG_TITLE = "Error: nombre demasiado largo";
    public static final String ERROR_RESOURCE_NOT_FOUND_TITLE = "Error: recurso no encontrado";


    //UNAUTHORIZED
    public static final String ERROR_UNAUTHORIZED_TITLE = "Acceso no autorizado";
    public static final String ERROR_ACCESS_DENIED_TITLE = "Acceso Denegado";
    public static final String ERROR_UNAUTHORIZED_DESCRIPTION = "No tiene permisos para acceder a este recurso";
    public static final String ERROR_INTERNAL_SERVER_TITLE = "Error Interno del Servidor";


    public static final String ERROR_FIELD_DETAIL = "Campo '%s': %s (valor rechazado: '%s')";
    public static final String ERROR_FIELD_DETAIL_NO_VALUE = "Campo '%s': %s";
    public static final String ERROR_TYPE_MISMATCH_DETAIL = "Error al convertir el parámetro '%s' de tipo '%s' a tipo '%s'";
    public static final String ERROR_TYPE_MISMATCH_NULL_STRING = ". Se recibió el texto 'null' pero se esperaba un valor numérico.";
    public static final String ERROR_NULL_REFERENCE_DETAIL = "Se intentó acceder a un objeto que no existe: %s";


    public static final String ERROR_UNAUTHORIZED_DETAIL_JWT = "Se requiere un token de autenticación JWT válido para acceder a este recurso.";
    public static final String ERROR_UNAUTHORIZED_DETAIL_GENERIC = "No autenticado";
    public static final String ERROR_ACCESS_DENIED_DETAIL = "Permisos insuficientes para acceder a este recurso";
    public static final String ERROR_INTERNAL_SERVER_DETAIL = "Ocurrió un error inesperado procesando la solicitud.";
    public static final String ERROR_INTERNAL_SERVER_NULL_DETAIL = "Ocurrió un error interno inesperado (referencia nula).";


}
