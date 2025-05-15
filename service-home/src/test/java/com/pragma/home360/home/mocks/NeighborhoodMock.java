package com.pragma.home360.home.mocks;

import com.pragma.home360.home.domain.model.NeighborhoodModel;

/**
 * Clase de utilidad para crear instancias de NeighborhoodModel para pruebas
 */
public class NeighborhoodMock {

    /**
     * Crea un modelo de barrio con los parámetros especificados
     *
     * @param id          Identificador del barrio
     * @param name        Nombre del barrio
     * @param description Descripción del barrio
     * @param cityId      Identificador de la ciudad a la que pertenece
     * @return Una instancia de NeighborhoodModel con los valores proporcionados
     */
    public static NeighborhoodModel createNeighborhoodModel(Long id, String name, String description, Long cityId) {
        NeighborhoodModel neighborhoodModel = new NeighborhoodModel();
        neighborhoodModel.setId(id);
        neighborhoodModel.setName(name);
        neighborhoodModel.setDescription(description);
        neighborhoodModel.setCityId(cityId);
        return neighborhoodModel;
    }

    /**
     * Crea un modelo de barrio con valores predeterminados
     *
     * @return Una instancia de NeighborhoodModel con valores predeterminados
     */
    public static NeighborhoodModel createDefaultNeighborhoodModel() {
        return createNeighborhoodModel(1L, "Barrio Test", "Descripción de barrio para pruebas", 1L);
    }

    /**
     * Crea un modelo de barrio con un ID específico
     *
     * @param id Identificador del barrio
     * @return Una instancia de NeighborhoodModel con el ID especificado
     */
    public static NeighborhoodModel createNeighborhoodModelWithId(Long id) {
        return createNeighborhoodModel(id, "Barrio Test", "Descripción de barrio para pruebas", 1L);
    }

    /**
     * Crea un modelo de barrio con un nombre específico
     *
     * @param name Nombre del barrio
     * @return Una instancia de NeighborhoodModel con el nombre especificado
     */
    public static NeighborhoodModel createNeighborhoodModelWithName(String name) {
        return createNeighborhoodModel(1L, name, "Descripción de barrio para pruebas", 1L);
    }

    /**
     * Crea un modelo de barrio con una descripción específica
     *
     * @param description Descripción del barrio
     * @return Una instancia de NeighborhoodModel con la descripción especificada
     */
    public static NeighborhoodModel createNeighborhoodModelWithDescription(String description) {
        return createNeighborhoodModel(1L, "Barrio Test", description, 1L);
    }

    /**
     * Crea un modelo de barrio con una ciudad específica
     *
     * @param cityId ID de la ciudad
     * @return Una instancia de NeighborhoodModel con la ciudad especificada
     */
    public static NeighborhoodModel createNeighborhoodModelWithCityId(Long cityId) {
        return createNeighborhoodModel(1L, "Barrio Test", "Descripción de barrio para pruebas", cityId);
    }

    /**
     * Crea un barrio con un nombre vacío (para pruebas de validación)
     *
     * @return Una instancia de NeighborhoodModel con nombre vacío
     */
    public static NeighborhoodModel createNeighborhoodModelWithEmptyName() {
        return createNeighborhoodModel(1L, "", "Descripción de barrio para pruebas", 1L);
    }

    /**
     * Crea un barrio con una descripción vacía (para pruebas de validación)
     *
     * @return Una instancia de NeighborhoodModel con descripción vacía
     */
    public static NeighborhoodModel createNeighborhoodModelWithEmptyDescription() {
        return createNeighborhoodModel(1L, "Barrio Test", "", 1L);
    }

    /**
     * Crea un barrio sin ciudad (para pruebas de validación)
     *
     * @return Una instancia de NeighborhoodModel sin ciudad
     */
    public static NeighborhoodModel createNeighborhoodModelWithNullCityId() {
        return createNeighborhoodModel(1L, "Barrio Test", "Descripción de barrio para pruebas", null);
    }
}