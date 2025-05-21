package com.pragma.home360.home.infrastructure.exceptionshandler;

import com.pragma.home360.home.domain.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {

        List<FieldErrorDetail> fieldErrors = exception.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    if (error instanceof FieldError fieldError) {
                        return new FieldErrorDetail(
                                fieldError.getField(),
                                fieldError.getDefaultMessage(),
                                fieldError.getRejectedValue()
                        );
                    }
                    return new FieldErrorDetail("global", error.getDefaultMessage(), null);
                })
                .collect(Collectors.toList());

        ApiErrorCode errorCode = ApiErrorCode.VALIDATION_ERROR;
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionResponse(
                        false,
                        errorCode.getCode(),
                        errorCode.getDefaultMessage(),
                        LocalDateTime.now(),
                        fieldErrors
                )
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException exception) {

        ApiErrorCode errorCode = ApiErrorCode.INVALID_PARAMETER_FORMAT;
        String parameterName = exception.getName();
        String rejectedValueType = (exception.getValue() != null ? exception.getValue().getClass().getSimpleName() : "null");
        String expectedType = (exception.getRequiredType() != null ? exception.getRequiredType().getSimpleName() : "desconocido");

        String details = String.format("Error al convertir el parámetro '%s'. Se recibió un valor de tipo '%s' (valor: '%s') pero se esperaba un tipo '%s'.",
                parameterName,
                rejectedValueType,
                Objects.toString(exception.getValue(), "null"),
                expectedType);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionResponse(
                        false,
                        errorCode.getCode(),
                        String.format(errorCode.getDefaultMessage(), parameterName),
                        LocalDateTime.now(),
                        details
                )
        );
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(ValidationException exception) {
        ApiErrorCode errorCode = ApiErrorCode.VALIDATION_ERROR;
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                new ExceptionResponse(
                        false,
                        errorCode.getCode(),
                        errorCode.getDefaultMessage(),
                        LocalDateTime.now(),
                        exception.getMessage()
                )
        );
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleAlreadyExistsException(AlreadyExistsException exception) {
        ApiErrorCode errorCode = ApiErrorCode.ALREADY_EXISTS;
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ExceptionResponse(
                        false,
                        errorCode.getCode(),
                        errorCode.getDefaultMessage(),
                        LocalDateTime.now(),
                        exception.getMessage()
                )
        );
    }


    @ExceptionHandler(ModelNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleModelNotFoundException(ModelNotFoundException exception) {
        ApiErrorCode errorCode = ApiErrorCode.RESOURCE_NOT_FOUND;
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ExceptionResponse(
                        false,
                        errorCode.getCode(),
                        errorCode.getDefaultMessage(),
                        LocalDateTime.now(),
                        exception.getMessage()
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericException(Exception exception) {
        ApiErrorCode errorCode = ApiErrorCode.GENERIC_ERROR;
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ExceptionResponse(
                        false,
                        errorCode.getCode(),
                        errorCode.getDefaultMessage(),
                        LocalDateTime.now(),
                        "Ocurrió un error interno. Por favor, intente más tarde."
                )
        );
    }
}
