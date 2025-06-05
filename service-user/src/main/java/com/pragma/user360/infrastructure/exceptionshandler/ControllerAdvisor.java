package com.pragma.user360.infrastructure.exceptionshandler;


import com.pragma.user360.domain.exceptions.AlreadyExistsException;
import com.pragma.user360.domain.exceptions.ModelNotFoundException;
import com.pragma.user360.domain.exceptions.ValidationException;
import com.pragma.user360.infrastructure.utils.constants.InfraestructureConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
final public class ControllerAdvisor {

    private static final Logger log = LoggerFactory.getLogger(ControllerAdvisor.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<ValidationExceptionResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {

        List<ValidationErrorDetail> errorDetails = exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ValidationErrorDetail(
                        fieldError.getField(),
                        fieldError.getDefaultMessage(),
                        fieldError.getRejectedValue() != null ? fieldError.getRejectedValue().toString() : null
                ))
                .toList();

        ValidationExceptionResponse response = new ValidationExceptionResponse(
                false,
                InfraestructureConstants.ERROR_VALIDATION_TITLE,
                LocalDateTime.now(),
                errorDetails
        );

        return ResponseEntity.unprocessableEntity().body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException exception) {

        String details = String.format(InfraestructureConstants.ERROR_TYPE_MISMATCH_DETAIL,
                exception.getName(),
                (exception.getValue() != null ? exception.getValue().getClass().getSimpleName() : "null"),
                exception.getRequiredType() != null ? exception.getRequiredType().getSimpleName() : "desconocido"); // Added null check for requiredType

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
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                new ExceptionResponse(
                        false,
                        InfraestructureConstants.ERROR_VALIDATION_GENERIC_TITLE,
                        LocalDateTime.now(),
                        exception.getMessage()
                )
        );
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionResponse> handleNullPointerException(NullPointerException exception) {
        log.error("NullPointerException capturada: ", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ExceptionResponse(
                        false,
                        InfraestructureConstants.ERROR_NULL_REFERENCE_TITLE,
                        LocalDateTime.now(),
                        InfraestructureConstants.ERROR_INTERNAL_SERVER_NULL_DETAIL
                )
        );
    }

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ExceptionResponse> handleAlreadyExistsException(AlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ExceptionResponse(
                        false,
                        exception.getMessage(),
                        LocalDateTime.now(),
                        null
                )
        );
    }


    @ExceptionHandler(ModelNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionResponse> handleModelNotFoundException(ModelNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ExceptionResponse(
                        false,
                        exception.getMessage(),
                        LocalDateTime.now(),
                        null
                )
        );
    }


    @ExceptionHandler(LockedException.class)
    @ResponseStatus(HttpStatus.LOCKED)
    public ResponseEntity<ExceptionResponse> handleLockedException(LockedException exception) {
        log.warn("Usuario bloqueado: ", exception);
        return ResponseEntity.status(HttpStatus.LOCKED).body(
                new ExceptionResponse(
                        false,
                        InfraestructureConstants.ERROR_USER_LOCKED_TITLE,
                        LocalDateTime.now(),
                        InfraestructureConstants.ERROR_USER_LOCKED_DETAIL
                )
        );
    }






    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionResponse> handleGenericException(Exception exception) {
        log.error("Error inesperado capturado: ", exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ExceptionResponse(
                        false,
                        exception.getMessage(),
                        LocalDateTime.now(),
                        null
                )
        );
    }
}
