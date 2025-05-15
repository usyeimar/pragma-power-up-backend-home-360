package com.pragma.user360.domain.validation;

import com.pragma.user360.domain.exceptions.ValidationException;
import com.pragma.user360.domain.utils.constants.DomainConstants;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import java.util.regex.Pattern;

public final class UserValidator {

    private static final int ADULT_AGE = 18;
    private static final int MINIMUM_PASSWORD_LENGTH = 8;

    private UserValidator() {
        throw new IllegalStateException("Utility class for domain validation");
    }

    private enum ValidationRegex {
        NUMERIC("^[0-9]+$"),
        PHONE("^\\+?[0-9]{7,12}$"),
        EMAIL("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

        private final Pattern pattern;

        ValidationRegex(String regex) {
            this.pattern = Pattern.compile(regex);
        }

        boolean matches(String value) {
            return value != null && pattern.matcher(value).matches();
        }
    }

    private abstract static class ValidationRule<T> {
        protected final T value;
        protected String errorMessage = "Validation failed";

        protected ValidationRule(T value) {
            this.value = value;
        }

        public ValidationRule<T> withMessage(String message) {
            this.errorMessage = Objects.requireNonNullElse(message, "Validation error message cannot be null");
            return this;
        }

        public ValidationRule<T> notNull() {
            if (value == null) {
                throw new ValidationException(errorMessage);
            }
            return this;
        }

        static StringValidationRule forString(String value) {
            return new StringValidationRule(value);
        }

        static DateValidationRule forDate(LocalDate value) {
            return new DateValidationRule(value);
        }
    }

    private static class StringValidationRule extends ValidationRule<String> {

        private StringValidationRule(String value) {
            super(value);
        }

        @Override
        public StringValidationRule withMessage(String message) {
            super.withMessage(message);
            return this;
        }

        @Override
        public StringValidationRule notNull() {
            super.notNull();
            return this;
        }

        public StringValidationRule notEmpty() {
            if (value == null || value.trim().isEmpty()) {
                throw new ValidationException(errorMessage);
            }
            return this;
        }

        public StringValidationRule matchesPattern(ValidationRegex regex) {
            if (!regex.matches(value)) {
                throw new ValidationException(errorMessage);
            }
            return this;
        }

        public StringValidationRule maxLength(int maxLength, String fieldName) {
            if (value != null && value.length() > maxLength) {
                throw new ValidationException(
                        String.format("El campo '%s' no puede exceder los %d caracteres.", fieldName, maxLength)
                );
            }
            return this;
        }

        public StringValidationRule minLength(int minLength) {
            if (value == null || value.length() < minLength) {
                throw new ValidationException(errorMessage);
            }
            return this;
        }
    }

    private static class DateValidationRule extends ValidationRule<LocalDate> {

        private DateValidationRule(LocalDate value) {
            super(value);
        }

        @Override
        public DateValidationRule withMessage(String message) {
            super.withMessage(message);
            return this;
        }

        @Override
        public DateValidationRule notNull() {
            super.notNull();
            return this;
        }

        public DateValidationRule isAdult() {
            if (value != null && Period.between(value, LocalDate.now()).getYears() < ADULT_AGE) {
                throw new ValidationException(errorMessage);
            }
            return this;
        }
    }

    public static void validateIsAdult(LocalDate birthDate) {
        ValidationRule.forDate(birthDate)
                .withMessage(DomainConstants.USER_BIRTH_DATE_CANNOT_BE_NULL)
                .notNull()
                .withMessage(DomainConstants.USER_MUST_BE_ADULT)
                .isAdult();
    }

    public static void validateDocumentId(String documentId) {
        ValidationRule.forString(documentId)
                .withMessage(DomainConstants.USER_DOCUMENT_ID_CANNOT_BE_EMPTY)
                .notEmpty()
                .withMessage(DomainConstants.USER_DOCUMENT_ID_MUST_BE_NUMERIC)
                .matchesPattern(ValidationRegex.NUMERIC)
                .maxLength(DomainConstants.USER_DOCUMENT_ID_MAX_LENGTH, "documento de identidad");
    }

    public static void validatePhoneNumber(String phoneNumber) {
        ValidationRule.forString(phoneNumber)
                .withMessage(DomainConstants.USER_PHONE_NUMBER_CANNOT_BE_EMPTY)
                .notEmpty()
                .withMessage(DomainConstants.USER_PHONE_NUMBER_INVALID_FORMAT)
                .matchesPattern(ValidationRegex.PHONE)
                .maxLength(DomainConstants.USER_PHONE_NUMBER_MAX_LENGTH, "número de teléfono");
    }

    public static void validateEmail(String email) {
        ValidationRule.forString(email)
                .withMessage(DomainConstants.USER_EMAIL_CANNOT_BE_EMPTY)
                .notEmpty()
                .withMessage(DomainConstants.USER_EMAIL_INVALID_FORMAT)
                .matchesPattern(ValidationRegex.EMAIL)
                .maxLength(DomainConstants.USER_EMAIL_MAX_LENGTH, "correo electrónico");
    }

    public static void validatePassword(String password) {
        ValidationRule.forString(password)
                .withMessage(DomainConstants.USER_PASSWORD_CANNOT_BE_EMPTY)
                .notEmpty()
                .withMessage(DomainConstants.USER_PASSWORD_TOO_SHORT)
                .minLength(MINIMUM_PASSWORD_LENGTH);
    }

    public static void validateFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new ValidationException(DomainConstants.USER_FIRST_NAME_CANNOT_BE_EMPTY);
        }
    }

    public static void validateLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new ValidationException(DomainConstants.USER_LAST_NAME_CANNOT_BE_EMPTY);
        }
    }
}
