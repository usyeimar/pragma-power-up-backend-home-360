package com.pragma.gateway360.shared.constants;


public final class GatewayConstants {

    private GatewayConstants() {
    }

    public static final String ERROR_CODE_AUTH_GENERIC = "AUTH_001";
    public static final String ERROR_TITLE_AUTH_GENERIC = "Error de Autenticación";
    public static final String ERROR_DETAILS_AUTH_GENERIC = "Acceso denegado. Por favor, proporcione un token de autenticación válido.";

    public static final String ERROR_CODE_INVALID_BEARER_TOKEN = "AUTH_002";
    public static final String ERROR_TITLE_INVALID_BEARER_TOKEN = "Token Bearer Inválido";
    public static final String ERROR_DETAILS_INVALID_BEARER_TOKEN_PREFIX = "El token bearer proporcionado está malformado o es inválido. ";

    public static final String ERROR_CODE_JWT_AUTH_FAILED = "AUTH_003";
    public static final String ERROR_TITLE_JWT_AUTH_FAILED = "Fallo en Autenticación JWT";

    public static final String ERROR_CODE_JWT_VALIDATION_FAILED = "JWT_VALIDATION_FAILED";
    public static final String ERROR_TITLE_JWT_VALIDATION_FAILED = "Fallo en Validación JWT";
    public static final String ERROR_DETAILS_JWT_VALIDATION_FAILED_PREFIX = "El JWT es inválido: ";

    public static final String ERROR_CODE_JWT_MISSING = "JWT_MISSING";
    public static final String ERROR_TITLE_JWT_MISSING = "JWT Faltante";
    public static final String ERROR_DETAILS_JWT_MISSING = "El JWT es faltante. Por favor, incluya un token válido en el encabezado Authorization.";

    public static final String ERROR_DETAILS_AUTH_FAILED_DUE_TO_PREFIX = "Fallo en autenticación debido a: ";
    public static final String LOG_ERROR_WRITING_AUTH_RESPONSE = "Error escribiendo la respuesta de error de autenticación al flujo de salida";

    public static final String JWT_ILLEGAL_STATE_SECRET_MISSING = "JWT Secret ('app.jwt.secret') is missing or empty. This is required for API Gateway JwtDecoder.";
    public static final String JWT_ILLEGAL_STATE_SHARED_SECRET_MISSING = "JWT Secret ('app.jwt.secret') is missing or empty for SharedSecretKey.";

}
