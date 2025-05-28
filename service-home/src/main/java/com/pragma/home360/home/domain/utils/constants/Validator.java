package com.pragma.home360.home.domain.utils.constants;

import com.pragma.home360.home.domain.exceptions.ValidationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Predicate;

public class Validator {

    Validator() {
    }

    public static void validateNotEmpty(String field, String errorMessage) {
        validate(field != null && !field.trim().isEmpty(), errorMessage, null);
    }

    public static void validateMaxLength(String field, int maxLength, String errorMessage) {
        if (field == null) {
            return;
        }
        validate(field.length() <= maxLength, String.format(errorMessage, maxLength), null);
    }

    public static void validate(boolean condition, String errorMessage, String targetExceptionClass) {
        if (!condition) {
            if (targetExceptionClass != null) {
                try {
                    Class<?> exceptionClass = Class.forName(targetExceptionClass);
                    if (!RuntimeException.class.isAssignableFrom(exceptionClass)) {
                        throw new IllegalArgumentException(String.format(DomainConstants.VALIDATOR_CUSTOM_EXCEPTION_NOT_RUNTIME, targetExceptionClass));
                    }
                    Constructor<?> constructor = exceptionClass.getConstructor(String.class);
                    throw (RuntimeException) constructor.newInstance(errorMessage);

                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException(String.format(DomainConstants.VALIDATOR_CUSTOM_EXCEPTION_CLASS_NOT_FOUND, targetExceptionClass), e);
                } catch (NoSuchMethodException e) {
                    throw new IllegalArgumentException(String.format(DomainConstants.VALIDATOR_CUSTOM_EXCEPTION_CONSTRUCTOR_NOT_FOUND, targetExceptionClass), e);
                } catch (InstantiationException e) {
                    throw new IllegalArgumentException(String.format(DomainConstants.VALIDATOR_CUSTOM_EXCEPTION_INSTANTIATION_FAILED, targetExceptionClass), e);
                } catch (IllegalAccessException e) {
                    throw new IllegalArgumentException(String.format(DomainConstants.VALIDATOR_CUSTOM_EXCEPTION_ILLEGAL_ACCESS, targetExceptionClass), e);
                } catch (InvocationTargetException e) {
                    Throwable cause = e.getCause();
                    if (cause instanceof RuntimeException) {
                        throw (RuntimeException) cause;
                    } else if (cause != null) {
                        throw new RuntimeException(String.format(DomainConstants.VALIDATOR_CUSTOM_EXCEPTION_CONSTRUCTOR_THREW_CHECKED, targetExceptionClass, cause.getMessage()), cause);
                    } else {
                        throw new RuntimeException(String.format(DomainConstants.VALIDATOR_CUSTOM_EXCEPTION_INVOCATION_TARGET_NO_CAUSE, targetExceptionClass), e);
                    }
                }
            } else {
                throw new ValidationException(errorMessage);
            }
        }
    }

    public static <T> void validateCustom(Predicate<T> condition, T value, String errorMessageTemplate, String targetExceptionClass) {
        if (value == null) {
            if (!condition.test(null)) {
                String finalErrorMessage = errorMessageTemplate;
                if (errorMessageTemplate != null && errorMessageTemplate.contains("%s")) {
                    try {
                        finalErrorMessage = String.format(errorMessageTemplate, (Object) null);
                    } catch (java.util.IllegalFormatException ife) {
                    }
                }
                validate(false, finalErrorMessage, targetExceptionClass);
            }
            return;
        }

        if (!condition.test(value)) {
            String finalErrorMessage = errorMessageTemplate;
            if (errorMessageTemplate != null &&
                    (errorMessageTemplate.contains("%s") || errorMessageTemplate.contains("%d") ||
                            errorMessageTemplate.contains("%f") || errorMessageTemplate.contains("%b") ||
                            errorMessageTemplate.contains("%c") || errorMessageTemplate.contains("%tB") ||
                            errorMessageTemplate.matches(".*%[.\\d]*[dfgeatbhnxsc%].*"))) {
                try {
                    finalErrorMessage = String.format(errorMessageTemplate, value);
                } catch (java.util.IllegalFormatException ife) {
                    System.err.println(String.format(DomainConstants.VALIDATOR_WARN_MESSAGE_FORMATTING_FAILED, errorMessageTemplate, value, ife.getMessage()));
                }
            }
            validate(false, finalErrorMessage, targetExceptionClass);
        }
    }
}