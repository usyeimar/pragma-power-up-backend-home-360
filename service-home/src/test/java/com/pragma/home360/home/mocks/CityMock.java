package com.pragma.home360.home.mocks;

import com.pragma.home360.home.domain.model.CityModel;

/**
 * Clase de utilidad para crear instancias de CityModel para pruebas
 */
public class CityMock {

    /**
     * Crea un modelo de ciudad con los parámetros especificados
     *
     * @param id           Identificador de la ciudad
     * @param name         Nombre de la ciudad
     * @param description  Descripción de la ciudad
     * @param departmentId Identificador del departamento al que pertenece
     * @return Una instancia de CityModel con los valores proporcionados
     */
    public static CityModel createCityModel(Long id, String name, String description, Long departmentId) {
        CityModel cityModel = new CityModel();
        cityModel.setId(id);
        cityModel.setName(name);
        cityModel.setDescription(description);
        cityModel.setDepartmentId(departmentId);
        return cityModel;
    }

    /**
     * Crea un modelo de ciudad con valores predeterminados
     *
     * @return Una instancia de CityModel con valores predeterminados
     */
    public static CityModel createDefaultCityModel() {
        return createCityModel(1L, "Ciudad Test", "Descripción de ciudad para pruebas", 1L);
    }

    /**
     * Crea un modelo de ciudad con un ID específico
     *
     * @param id Identificador de la ciudad
     * @return Una instancia de CityModel con el ID especificado
     */
    public static CityModel createCityModelWithId(Long id) {
        return createCityModel(id, "Ciudad Test", "Descripción de ciudad para pruebas", 1L);
    }

    /**
     * Crea un modelo de ciudad con un nombre específico
     *
     * @param name Nombre de la ciudad
     * @return Una instancia de CityModel con el nombre especificado
     */
    public static CityModel createCityModelWithName(String name) {
        return createCityModel(1L, name, "Descripción de ciudad para pruebas", 1L);
    }

    /**
     * Crea un modelo de ciudad con una descripción específica
     *
     * @param description Descripción de la ciudad
     * @return Una instancia de CityModel con la descripción especificada
     */
    public static CityModel createCityModelWithDescription(String description) {
        return createCityModel(1L, "Ciudad Test", description, 1L);
    }

    /**
     * Crea un modelo de ciudad con un departamento específico
     *
     * @param departmentId ID del departamento
     * @return Una instancia de CityModel con el departamento especificado
     */
    public static CityModel createCityModelWithDepartmentId(Long departmentId) {
        return createCityModel(1L, "Ciudad Test", "Descripción de ciudad para pruebas", departmentId);
    }

    /**
     * Crea una ciudad con un nombre vacío (para pruebas de validación)
     *
     * @return Una instancia de CityModel con nombre vacío
     */
    public static CityModel createCityModelWithEmptyName() {
        return createCityModel(1L, "", "Descripción de ciudad para pruebas", 1L);
    }

    /**
     * Crea una ciudad con una descripción vacía (para pruebas de validación)
     *
     * @return Una instancia de CityModel con descripción vacía
     */
    public static CityModel createCityModelWithEmptyDescription() {
        return createCityModel(1L, "Ciudad Test", "", 1L);
    }

    /**
     * Crea una ciudad sin departamento (para pruebas de validación)
     *
     * @return Una instancia de CityModel sin departamento
     */
    public static CityModel createCityModelWithNullDepartmentId() {
        return createCityModel(1L, "Ciudad Test", "Descripción de ciudad para pruebas", null);
    }
}