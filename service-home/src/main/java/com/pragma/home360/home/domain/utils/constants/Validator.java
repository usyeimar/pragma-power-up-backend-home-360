package com.pragma.home360.home.domain.utils.constants;

import com.pragma.home360.home.domain.exceptions.ValidationException;

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
     * Lanza una {@link ValidationException} si la condición proporcionada es falsa.
     *
     * @param condition    Condición booleana a validar.
     * @param errorMessage Mensaje de error si la condición no se cumple.
     * @throws ValidationException si la condición es falsa.
     */
    public static void validate(boolean condition, String errorMessage, String targetExceptionClass) {
        if (!condition) {
            if (targetExceptionClass != null) {
                try {
                    Class<?> exceptionClass = Class.forName(targetExceptionClass);

                    if (!RuntimeException.class.isAssignableFrom(exceptionClass)) {
                        throw new IllegalArgumentException("La clase proporcionada no extiende RuntimeException.");
                    }

                    throw (RuntimeException)
                            exceptionClass.getConstructor(String.class).newInstance(errorMessage);

                } catch (Exception e) {
                    throw new IllegalArgumentException(
                            "Error al instanciar la excepción personalizada: "
                                    + targetExceptionClass +
                                    e.getMessage()

                    );
                }
            }

            // Si no se proporciona clase personalizada, lanza excepción por defecto
            throw new ValidationException(errorMessage);
        }
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

    /**
     * Ejecuta una validación personalizada mediante un {@link Predicate}.
     *
     * @param condition    Predicado que define la condición personalizada.
     * @param value        Valor sobre el cual se aplica la validación.
     * @param errorMessage Mensaje de error si la validación falla.
     * @param <T>          Tipo del valor a validar.
     */
    public static <T> void validateCustom(Predicate<T> condition, T value, String errorMessage, String targetExceptionClass) {
        if (value == null) {
            try {
                if (condition.test(null)) {
                    return;
                }

            } catch (NullPointerException e) {
                validate(false, errorMessage, targetExceptionClass);
                return;
            }
        }
        validate(condition.test(value), errorMessage, targetExceptionClass);
    }
}
