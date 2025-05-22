package com.pragma.home360.home.domain.utils.constants;

import com.pragma.home360.home.domain.exceptions.ValidationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Predicate;

/**
 * Clase utilitaria para validaciones dentro del dominio de la aplicación.
 *
 * <p>La clase {@code Validator} proporciona métodos estáticos para validar
 * condiciones comunes y lanzar una {@link ValidationException} si la condición no se cumple.
 * Esto permite centralizar la lógica de validación y mantener el código limpio y reutilizable.
 *
 * <p>Validaciones disponibles:
 * <ul>
 *     <li>Validar condiciones booleanas generales.</li>
 *     <li>Validar que un campo de texto no esté vacío o nulo.</li>
 *     <li>Validar que un campo de texto no exceda una longitud máxima.</li>
 *     <li>Validaciones personalizadas mediante {@link Predicate}.</li>
 * </ul>
 *
 * <p><strong>Ejemplo de uso:</strong>
 * <pre>{@code
 *     Validator.validateNotEmpty(nombre, "El nombre no puede estar vacío");
 *     Validator.validateMaxLength(descripcion, 100, "La descripción no debe exceder %d caracteres");
 *     Validator.validateCustom(valor -> valor > 0, cantidad, "La cantidad debe ser mayor que cero");
 * }</pre>
 *
 * @author YEIMAR YECID LEMUS ROMAÑA
 */
public class Validator {

    Validator() {
    }


    /**
     * Valida que un campo de texto no sea nulo ni esté vacío (luego de aplicar {@code trim()}).
     *
     * @param field        El valor a validar.
     * @param errorMessage Mensaje de error si el campo es nulo o vacío.
     */
    public static void validateNotEmpty(String field, String errorMessage) {
        validate(field != null && !field.trim().isEmpty(), errorMessage, null);
    }

    /**
     * Valida que un campo de texto no exceda la longitud máxima especificada.
     *
     * @param field        El valor a validar.
     * @param maxLength    Longitud máxima permitida.
     * @param errorMessage Mensaje de error si se excede la longitud.
     *                     Puede contener un marcador de posición para el número máximo (ej: "%d").
     */
    public static void validateMaxLength(String field, int maxLength, String errorMessage) {

        validate(field.length() <= maxLength, String.format(errorMessage, maxLength), null);
    }


    public static void validate(boolean condition, String errorMessage, String targetExceptionClass) {
        if (!condition) {
            if (targetExceptionClass != null) {
                try {
                    Class<?> exceptionClass = Class.forName(targetExceptionClass);
                    if (!RuntimeException.class.isAssignableFrom(exceptionClass)) {
                        throw new IllegalArgumentException("La clase de excepción personalizada '" + targetExceptionClass + "' no extiende RuntimeException.");
                    }
                    Constructor<?> constructor = exceptionClass.getConstructor(String.class);
                    throw (RuntimeException) constructor.newInstance(errorMessage);

                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException("Clase de excepción personalizada no encontrada: " + targetExceptionClass, e);
                } catch (NoSuchMethodException e) {
                    throw new IllegalArgumentException("Constructor (String) no encontrado para la clase de excepción personalizada: " + targetExceptionClass, e);
                } catch (InstantiationException e) {
                    throw new IllegalArgumentException("No se pudo instanciar la clase de excepción personalizada (puede ser abstracta o interfaz): " + targetExceptionClass, e);
                } catch (IllegalAccessException e) {
                    throw new IllegalArgumentException("Acceso ilegal al constructor de la clase de excepción personalizada: " + targetExceptionClass, e);
                } catch (InvocationTargetException e) {
                    Throwable cause = e.getCause();
                    if (cause instanceof RuntimeException) {
                        throw (RuntimeException) cause;
                    } else if (cause != null) {
                        throw new RuntimeException("El constructor de la excepción personalizada '" + targetExceptionClass + "' lanzó una excepción chequeada: " + cause.getMessage(), cause);
                    } else {
                        throw new RuntimeException("InvocationTargetException sin causa al llamar al constructor de '" + targetExceptionClass + "'.", e);
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
                        // Silently ignore if formatting "null" fails for a non-%s template
                    }
                }
                validate(false, finalErrorMessage, targetExceptionClass);
            }
            return;
        }

        if (!condition.test(value)) {
            String finalErrorMessage = errorMessageTemplate;
            if (errorMessageTemplate != null && (errorMessageTemplate.contains("%s") || errorMessageTemplate.contains("%d") || errorMessageTemplate.contains("%f") || errorMessageTemplate.contains("%b") || errorMessageTemplate.contains("%c") || errorMessageTemplate.contains("%t"))) {
                try {
                    finalErrorMessage = String.format(errorMessageTemplate, value);
                } catch (java.util.IllegalFormatException ife) {
                    System.err.println("ADVERTENCIA: Falló la formatación del mensaje de error. Plantilla: '" + errorMessageTemplate + "', Valor: '" + value + "'. Error: " + ife.getMessage());
                }
            }
            validate(false, finalErrorMessage, targetExceptionClass);
        }
    }
}
