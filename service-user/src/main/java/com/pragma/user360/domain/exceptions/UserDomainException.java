package com.pragma.user360.domain.exceptions;

/**
 * Excepción específica del dominio para errores relacionados con usuarios.
 * Esta excepción encapsula errores de negocio relacionados con usuarios.
 */
public class UserDomainException extends RuntimeException {
    
    public UserDomainException(String message) {
        super(message);
    }

    public UserDomainException(String message, Throwable cause) {
        super(message, cause);
    }
} 