package com.pragma.user360.domain.utils.constants;


public final class DomainConstants {


    private DomainConstants() {
    }

    // Roles
    public static final String ROLE_VENDEDOR = "VENDEDOR";
    public static final String ROLE_CLIENTE = "CLIENTE";
    public static final String ROLE_ADMIN = "ADMINISTRADOR";
    public static final String ROLE_USER = "USER";
    public static final String ROLE_GUEST = "GUEST";


    // Validaciones generales
    public static final String USER_FIRST_NAME_CANNOT_BE_EMPTY = "El nombre del usuario no puede estar vacío.";
    public static final String USER_LAST_NAME_CANNOT_BE_EMPTY = "El apellido del usuario no puede estar vacío.";
    public static final String USER_DOCUMENT_ID_CANNOT_BE_EMPTY = "El documento de identidad no puede estar vacío.";
    public static final String USER_PHONE_NUMBER_CANNOT_BE_EMPTY = "El número de teléfono no puede estar vacío.";
    public static final String USER_BIRTH_DATE_CANNOT_BE_NULL = "La fecha de nacimiento no puede ser nula.";
    public static final String USER_EMAIL_CANNOT_BE_EMPTY = "El correo electrónico no puede estar vacío.";
    public static final String USER_PASSWORD_CANNOT_BE_EMPTY = "La contraseña no puede estar vacía.";

    // Validaciones específicas
    public static final String USER_MUST_BE_ADULT = "El usuario debe ser mayor de edad (18 años o más).";
    public static final String USER_DOCUMENT_ID_MUST_BE_NUMERIC = "El documento de identidad debe contener solo números.";
    public static final String USER_PHONE_NUMBER_INVALID_FORMAT = "El formato del número de teléfono es inválido. Debe contener entre 7 y 12 números, opcionalmente con un + al inicio.";
    public static final String USER_EMAIL_INVALID_FORMAT = "El formato del correo electrónico es inválido.";
    public static final String USER_PASSWORD_TOO_SHORT = "La contraseña debe tener al menos 8 caracteres.";

    // Excepciones
    public static final String USER_EMAIL_ALREADY_EXISTS = "Ya existe un usuario con el correo electrónico: %s";
    public static final String USER_DOCUMENT_ID_ALREADY_EXISTS = "Ya existe un usuario con el documento de identidad: %s";
    public static final String USER_NOT_FOUND = "No se encontró el usuario con ID: %d";

    // Longitudes máximas
    public static final int USER_FIRST_NAME_MAX_LENGTH = 50;
    public static final int USER_LAST_NAME_MAX_LENGTH = 50;
    public static final int USER_DOCUMENT_ID_MAX_LENGTH = 20;
    public static final int USER_PHONE_NUMBER_MAX_LENGTH = 13;
    public static final int USER_EMAIL_MAX_LENGTH = 100;
}
