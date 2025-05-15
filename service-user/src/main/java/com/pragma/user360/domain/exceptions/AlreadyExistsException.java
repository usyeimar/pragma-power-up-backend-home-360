package com.pragma.user360.domain.exceptions;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(
            String message
    ) {
        super(message);
    }
}
