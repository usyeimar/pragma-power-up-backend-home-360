package com.pragma.home360.home.mocks;

import com.pragma.home360.home.domain.model.LocationModel;

/**
 * Clase de utilidad para crear instancias de LocationModel para pruebas
 */
public class LocationMock {

    /**
     * Crea un modelo de ubicación con los parámetros especificados
     *
     * @param id             Identificador de la ubicación
     * @param address        Dirección de la ubicación
     * @param latitude       Latitud de la ubicación
     * @param longitude      Longitud de la ubicación
     * @param referencePoint Punto de referencia de la ubicación
     * @param neighborhoodId ID del barrio al que pertenece
     * @param cityId         ID de la ciudad a la que pertenece
     * @return Una instancia de LocationModel con los valores proporcionados
     */
    public static LocationModel createLocationModel(Long id, String address, Double latitude, Double longitude,
                                                    String referencePoint, Long neighborhoodId, Long cityId) {
        LocationModel locationModel = new LocationModel();
        locationModel.setId(id);
        locationModel.setAddress(address);
        locationModel.setLatitude(latitude);
        locationModel.setLongitude(longitude);
        locationModel.setReferencePoint(referencePoint);
        locationModel.setNeighborhoodId(neighborhoodId);
        locationModel.setCityId(cityId);
        return locationModel;
    }

    /**
     * Crea un modelo de ubicación con información adicional para búsquedas
     *
     * @param id               Identificador de la ubicación
     * @param address          Dirección de la ubicación
     * @param latitude         Latitud de la ubicación
     * @param longitude        Longitud de la ubicación
     * @param referencePoint   Punto de referencia de la ubicación
     * @param neighborhoodId   ID del barrio al que pertenece
     * @param neighborhoodName Nombre del barrio
     * @param cityId           ID de la ciudad
     * @param cityName         Nombre de la ciudad
     * @param departmentId     ID del departamento
     * @param departmentName   Nombre del departamento
     * @return Una instancia de LocationModel con toda la información para búsquedas
     */
    public static LocationModel createFullLocationModel(Long id, String address, Double latitude, Double longitude,
                                                        String referencePoint, Long neighborhoodId, String neighborhoodName,
                                                        Long cityId, String cityName, Long departmentId, String departmentName) {
        LocationModel locationModel = createLocationModel(id, address, latitude, longitude, referencePoint, neighborhoodId, cityId);
        locationModel.setNeighborhoodName(neighborhoodName);
        locationModel.setCityName(cityName);
        locationModel.setDepartmentId(departmentId);
        locationModel.setDepartmentName(departmentName);
        return locationModel;
    }

    /**
     * Crea un modelo de ubicación con valores predeterminados
     *
     * @return Una instancia de LocationModel con valores predeterminados
     */
    public static LocationModel createDefaultLocationModel() {
        return createLocationModel(1L, "Dirección Test", 4.6097, -74.0817,
                "Punto de referencia test", 1L, 1L);
    }

    /**
     * Crea un modelo completo de ubicación con valores predeterminados
     *
     * @return Una instancia completa de LocationModel con valores predeterminados
     */
    public static LocationModel createDefaultFullLocationModel() {
        return createFullLocationModel(1L, "Dirección Test", 4.6097, -74.0817,
                "Punto de referencia test", 1L, "Barrio Test",
                1L, "Ciudad Test", 1L, "Departamento Test");
    }

    /**
     * Crea un modelo de ubicación con un ID específico
     *
     * @param id Identificador de la ubicación
     * @return Una instancia de LocationModel con el ID especificado
     */
    public static LocationModel createLocationModelWithId(Long id) {
        return createLocationModel(id, "Dirección Test", 4.6097, -74.0817,
                "Punto de referencia test", 1L, 1L);
    }

    /**
     * Crea un modelo de ubicación con una dirección específica
     *
     * @param address Dirección de la ubicación
     * @return Una instancia de LocationModel con la dirección especificada
     */
    public static LocationModel createLocationModelWithAddress(String address) {
        return createLocationModel(1L, address, 4.6097, -74.0817,
                "Punto de referencia test", 1L, 1L);
    }

    /**
     * Crea un modelo de ubicación con coordenadas específicas
     *
     * @param latitude  Latitud de la ubicación
     * @param longitude Longitud de la ubicación
     * @return Una instancia de LocationModel con las coordenadas especificadas
     */
    public static LocationModel createLocationModelWithCoordinates(Double latitude, Double longitude) {
        return createLocationModel(1L, "Dirección Test", latitude, longitude,
                "Punto de referencia test", 1L, 1L);
    }

    /**
     * Crea un modelo de ubicación con un barrio específico
     *
     * @param neighborhoodId ID del barrio
     * @return Una instancia de LocationModel con el barrio especificado
     */
    public static LocationModel createLocationModelWithNeighborhood(Long neighborhoodId) {
        return createLocationModel(1L, "Dirección Test", 4.6097, -74.0817,
                "Punto de referencia test", neighborhoodId, 1L);
    }

    /**
     * Crea un modelo de ubicación con una ciudad específica
     *
     * @param cityId ID de la ciudad
     * @return Una instancia de LocationModel con la ciudad especificada
     */
    public static LocationModel createLocationModelWithCity(Long cityId) {
        return createLocationModel(1L, "Dirección Test", 4.6097, -74.0817,
                "Punto de referencia test", 1L, cityId);
    }

    /**
     * Crea una ubicación sin barrio (para pruebas de validación)
     *
     * @return Una instancia de LocationModel sin barrio
     */
    public static LocationModel createLocationModelWithNullNeighborhood() {
        return createLocationModel(1L, "Dirección Test", 4.6097, -74.0817,
                "Punto de referencia test", null, 1L);
    }

    /**
     * Crea una ubicación sin ciudad (para pruebas de validación)
     *
     * @return Una instancia de LocationModel sin ciudad
     */
    public static LocationModel createLocationModelWithNullCity() {
        return createLocationModel(1L, "Dirección Test", 4.6097, -74.0817,
                "Punto de referencia test", 1L, null);
    }
}