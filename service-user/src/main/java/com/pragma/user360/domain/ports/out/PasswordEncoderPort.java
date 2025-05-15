package com.pragma.user360.domain.ports.out;

/**
 * Puerto de salida para la codificación de contraseñas.
 * Define la interfaz que el dominio necesita para codificar contraseñas,
 * sin depender de implementaciones específicas.
 */
public interface PasswordEncoderPort {
    /**
     * Codifica una contraseña en texto plano.
     *
     * @param rawPassword La contraseña en texto plano a codificar
     * @return La contraseña codificada
     */
    String encode(String rawPassword);

    /**
     * Verifica si una contraseña en texto plano coincide con una contraseña codificada.
     *
     * @param rawPassword La contraseña en texto plano a verificar
     * @param encodedPassword La contraseña codificada contra la que verificar
     * @return true si las contraseñas coinciden, false en caso contrario
     */
    boolean matches(String rawPassword, String encodedPassword);
} 