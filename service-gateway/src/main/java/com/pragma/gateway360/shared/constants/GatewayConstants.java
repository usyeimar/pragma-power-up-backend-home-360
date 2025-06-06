package com.pragma.gateway360.shared.constants;

public final class GatewayConstants {

    private GatewayConstants() {
    }

    public static final String ERROR_CODE_AUTH_GENERIC = "AUTH_001";
    public static final String ERROR_TITLE_AUTH_GENERIC = "Error de Autenticacion";
    public static final String ERROR_DETAILS_AUTH_GENERIC = "Acceso denegado. Por favor, proporcione un token de autenticacion valido.";

    public static final String ERROR_CODE_INVALID_BEARER_TOKEN = "AUTH_002";
    public static final String ERROR_TITLE_INVALID_BEARER_TOKEN = "Token Bearer Invalido";
    public static final String ERROR_DETAILS_INVALID_BEARER_TOKEN_PREFIX = "El token bearer proporcionado esta malformado o es invalido. ";

    public static final String ERROR_CODE_JWT_AUTH_FAILED = "AUTH_003";
    public static final String ERROR_TITLE_JWT_AUTH_FAILED = "Fallo en Autenticacion JWT";

    public static final String ERROR_CODE_JWT_VALIDATION_FAILED = "JWT_VALIDATION_001"; // Código de error más específico
    public static final String ERROR_TITLE_JWT_VALIDATION_FAILED = "Fallo en Validacion JWT";
    public static final String ERROR_DETAILS_JWT_VALIDATION_FAILED_PREFIX = "El JWT es invalido: ";

    public static final String ERROR_CODE_JWT_MISSING = "JWT_VALIDATION_002";
    public static final String ERROR_TITLE_JWT_MISSING = "JWT Faltante";
    public static final String ERROR_DETAILS_JWT_MISSING = "El JWT es faltante. Por favor, incluya un token valido en el encabezado Authorization.";

    public static final String ERROR_DETAILS_AUTH_FAILED_DUE_TO_PREFIX = "Fallo en autenticacion debido a: ";
    public static final String LOG_ERROR_WRITING_AUTH_RESPONSE = "Error escribiendo la respuesta de error de autenticacion al flujo de salida";

    public static final String JWT_ILLEGAL_STATE_SECRET_MISSING = "JWT Secret ('app.jwt.secret') is missing or empty. This is required for API Gateway JwtDecoder.";
    public static final String JWT_ILLEGAL_STATE_SHARED_SECRET_MISSING = "JWT Secret ('app.jwt.secret') is missing or empty for SharedSecretKey.";

    public static final String ERROR_CODE_JWT_SIGNATURE_INVALID = "JWT_SIGNATURE_001";
    public static final String ERROR_TITLE_JWT_SIGNATURE_INVALID = "Firma de JWT Invalida";
    public static final String ERROR_DETAILS_JWT_SIGNATURE_INVALID = "La firma del token JWT no es valida. Verifique el secreto y el algoritmo del JWT.";

    public static final String ERROR_CODE_JWT_EXPIRED = "JWT_EXPIRATION_001";
    public static final String ERROR_TITLE_JWT_EXPIRED = "Token JWT Expirado";
    public static final String ERROR_DETAILS_JWT_EXPIRED = "El token JWT ha expirado. Por favor, inicie sesion de nuevo.";

    public static final String ERROR_CODE_JWT_MALFORMED = "JWT_FORMAT_001";
    public static final String ERROR_TITLE_JWT_MALFORMED = "Token JWT Malformado";
    public static final String ERROR_DETAILS_JWT_MALFORMED = "El token JWT no tiene un formato valido.";

    public static final String ERROR_CODE_JWT_UNSUPPORTED = "JWT_SUPPORT_001";
    public static final String ERROR_TITLE_JWT_UNSUPPORTED = "Token JWT No Soportado";
    public static final String ERROR_DETAILS_JWT_UNSUPPORTED = "El tipo de token JWT no es soportado.";

    public static final String ERROR_CODE_JWT_ISSUER_INVALID = "JWT_ISSUER_001";
    public static final String ERROR_TITLE_JWT_ISSUER_INVALID = "Emisor de JWT Invalido";
    public static final String ERROR_DETAILS_JWT_ISSUER_INVALID_PREFIX = "El emisor del token JWT es invalido. Esperado: ";
    public static final String ERROR_DETAILS_JWT_ISSUER_INVALID_SUFFIX = ", Recibido: ";


}
