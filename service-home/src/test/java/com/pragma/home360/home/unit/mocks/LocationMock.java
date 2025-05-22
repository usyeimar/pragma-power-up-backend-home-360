package com.pragma.home360.home.unit.mocks;

import com.pragma.home360.home.domain.model.CityModel;
import com.pragma.home360.home.domain.model.DepartmentModel;
import com.pragma.home360.home.domain.model.LocationModel;
import com.pragma.home360.home.domain.model.NeighborhoodModel;

public class LocationMock {

    public static LocationModel createLocationModel(Long id, String address, Double latitude, Double longitude,
                                                    String referencePoint, NeighborhoodModel neighborhood, CityModel city) {
        LocationModel locationModel = new LocationModel();
        locationModel.setId(id);
        locationModel.setAddress(address);
        locationModel.setLatitude(latitude);
        locationModel.setLongitude(longitude);
        locationModel.setReferencePoint(referencePoint);
        locationModel.setNeighborhood(neighborhood);
        locationModel.setCity(city);
        // Department is usually derived from City -> Neighborhood -> Department
        // If direct setting is needed, LocationModel should have a setDepartment method.
        // For now, assuming it's derived or not directly set here.
        return locationModel;
    }

    public static LocationModel createDefaultLocationModel() {
        DepartmentModel department = new DepartmentModel(1L, "Departamento Test", "Desc Depto");
        CityModel city = new CityModel(1L, "Ciudad Test", "Desc Ciudad", department);
        NeighborhoodModel neighborhood = new NeighborhoodModel(1L, "Barrio Test", "Desc Barrio", city);
        return createLocationModel(1L, "Dirección Test", 4.6097, -74.0817,
                "Punto de referencia test", neighborhood, city);
    }

    public static LocationModel createFullLocationModel(Long id, String address, Double latitude, Double longitude,
                                                        String referencePoint, NeighborhoodModel neighborhood,
                                                        CityModel city, DepartmentModel department) {
        LocationModel locationModel = createLocationModel(id, address, latitude, longitude, referencePoint, neighborhood, city);
        // If LocationModel needs to hold a direct DepartmentModel reference:
        // locationModel.setDepartment(department); // This assumes LocationModel has setDepartment
        return locationModel;
    }


    public static LocationModel createDefaultFullLocationModel() {
        DepartmentModel department = new DepartmentModel(1L, "Departamento Test", "Desc Depto");
        CityModel city = new CityModel(1L, "Ciudad Test", "Desc Ciudad", department);
        NeighborhoodModel neighborhood = new NeighborhoodModel(1L, "Barrio Test", "Desc Barrio", city);
        return createFullLocationModel(1L, "Dirección Test", 4.6097, -74.0817,
                "Punto de referencia test", neighborhood, city, department);
    }


    public static LocationModel createLocationModelWithId(Long id) {
        DepartmentModel department = new DepartmentModel(1L, "Departamento Test", "Desc Depto");
        CityModel city = new CityModel(1L, "Ciudad Test", "Desc Ciudad", department);
        NeighborhoodModel neighborhood = new NeighborhoodModel(1L, "Barrio Test", "Desc Barrio", city);
        return createLocationModel(id, "Dirección Test", 4.6097, -74.0817,
                "Punto de referencia test", neighborhood, city);
    }


    public static LocationModel createLocationModelWithAddress(String address) {
        DepartmentModel department = new DepartmentModel(1L, "Departamento Test", "Desc Depto");
        CityModel city = new CityModel(1L, "Ciudad Test", "Desc Ciudad", department);
        NeighborhoodModel neighborhood = new NeighborhoodModel(1L, "Barrio Test", "Desc Barrio", city);
        return createLocationModel(1L, address, 4.6097, -74.0817,
                "Punto de referencia test", neighborhood, city);
    }

    public static LocationModel createLocationModelWithCoordinates(Double latitude, Double longitude) {
        DepartmentModel department = new DepartmentModel(1L, "Departamento Test", "Desc Depto");
        CityModel city = new CityModel(1L, "Ciudad Test", "Desc Ciudad", department);
        NeighborhoodModel neighborhood = new NeighborhoodModel(1L, "Barrio Test", "Desc Barrio", city);
        return createLocationModel(1L, "Dirección Test", latitude, longitude,
                "Punto de referencia test", neighborhood, city);
    }

    public static LocationModel createLocationModelWithNeighborhood(NeighborhoodModel neighborhood) {
        CityModel city = neighborhood != null ? neighborhood.getCity() : null;
        return createLocationModel(1L, "Dirección Test", 4.6097, -74.0817,
                "Punto de referencia test", neighborhood, city);
    }

    public static LocationModel createLocationModelWithCity(CityModel city) {
        NeighborhoodModel neighborhood = new NeighborhoodModel(1L, "Barrio Test", "Desc Barrio", city);
        return createLocationModel(1L, "Dirección Test", 4.6097, -74.0817,
                "Punto de referencia test", neighborhood, city);
    }

    public static LocationModel createLocationModelWithNullNeighborhood() {
        DepartmentModel department = new DepartmentModel(1L, "Departamento Test", "Desc Depto");
        CityModel city = new CityModel(1L, "Ciudad Test", "Desc Ciudad", department);
        return createLocationModel(1L, "Dirección Test", 4.6097, -74.0817,
                "Punto de referencia test", null, city);
    }

    public static LocationModel createLocationModelWithNullCity() {
        return createLocationModel(1L, "Dirección Test", 4.6097, -74.0817,
                "Punto de referencia test", null, null);
    }
}
