package com.pragma.home360.home.infrastructure.exceptionshandler;

import com.pragma.home360.home.domain.exceptions.*;
import com.pragma.home360.home.infrastructure.utils.constants.InfraestructureConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {

        String details = exception.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    if (error instanceof FieldError fieldError) {
                        return fieldError.getRejectedValue() != null ?
                                String.format(InfraestructureConstants.ERROR_FIELD_DETAIL,
                                        fieldError.getField(),
                                        fieldError.getDefaultMessage(),
                                        fieldError.getRejectedValue()) :
                                String.format(InfraestructureConstants.ERROR_FIELD_DETAIL_NO_VALUE,
                                        fieldError.getField(),
                                        fieldError.getDefaultMessage());
                    }
                    return error.getDefaultMessage();
                })
                .collect(Collectors.joining(", "));

        return ResponseEntity.badRequest().body(
                new ExceptionResponse(
                        false,
                        InfraestructureConstants.ERROR_VALIDATION_TITLE,
                        LocalDateTime.now(),
                        details
                )
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException exception) {

        String details = String.format(InfraestructureConstants.ERROR_TYPE_MISMATCH_DETAIL,
                exception.getName(),
                (exception.getValue() != null ? exception.getValue().getClass().getSimpleName() : "null"),
                exception.getRequiredType().getSimpleName());

        if (exception.getValue() != null && exception.getValue().toString().equals("null")) {
            details += InfraestructureConstants.ERROR_TYPE_MISMATCH_NULL_STRING;
        }

        return ResponseEntity.badRequest().body(
                new ExceptionResponse(
                        false,
                        String.format(InfraestructureConstants.ERROR_TYPE_MISMATCH_TITLE, exception.getName()),
                        LocalDateTime.now(),
                        details
                )
        );
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<ExceptionResponse> handleValidationException(ValidationException exception) {
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(
                        false,
                        InfraestructureConstants.ERROR_VALIDATION_GENERIC_TITLE,
                        LocalDateTime.now(),
                        exception.getMessage()
                )
        );
    }

//    @ExceptionHandler(NullPointerException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseEntity<ExceptionResponse> handleNullPointerException(NullPointerException exception) {
//        return ResponseEntity.badRequest().body(
//                new ExceptionResponse(
//                        false,
//                        InfraestructureConstants.ERROR_NULL_REFERENCE_TITLE,
//                        LocalDateTime.now(),
//                        String.format(InfraestructureConstants.ERROR_NULL_REFERENCE_DETAIL, exception.getMessage())
//                )
//        );
//    }

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ExceptionResponse> handleAlreadyExistsException(AlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ExceptionResponse(
                        false,
                        InfraestructureConstants.ERROR_ALREADY_EXISTS_TITLE,
                        LocalDateTime.now(),
                        exception.getMessage()
                )
        );
    }


    @ExceptionHandler(ModelNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionResponse> handleModelNotFoundException(ModelNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ExceptionResponse(
                        false,
                        InfraestructureConstants.ERROR_RESOURCE_NOT_FOUND_TITLE,
                        LocalDateTime.now(),
                        exception.getMessage()
                )
        );
    }
}