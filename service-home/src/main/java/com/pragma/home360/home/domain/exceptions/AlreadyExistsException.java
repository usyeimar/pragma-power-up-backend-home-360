package com.pragma.home360.home.domain.exceptions;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(
            String message
    ) {
        super(message);
    }
}
