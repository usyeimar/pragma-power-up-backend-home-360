package com.pragma.home360.home.unit.mocks;

import com.pragma.home360.home.domain.model.CityModel;
import com.pragma.home360.home.domain.model.DepartmentModel;
import com.pragma.home360.home.domain.model.NeighborhoodModel;

public class NeighborhoodMock {

    public static NeighborhoodModel createNeighborhoodModel(Long id, String name, String description, CityModel city) {
        NeighborhoodModel neighborhoodModel = new NeighborhoodModel();
        neighborhoodModel.setId(id);
        neighborhoodModel.setName(name);
        neighborhoodModel.setDescription(description);
        neighborhoodModel.setCity(city);
        return neighborhoodModel;
    }

    public static NeighborhoodModel createDefaultNeighborhoodModel() {
        DepartmentModel department = new DepartmentModel(1L, "Departamento Test", "Desc Depto");
        CityModel city = new CityModel(1L, "Ciudad Test", "Desc Ciudad", department);
        return createNeighborhoodModel(1L, "Barrio Test", "Descripción de barrio para pruebas", city);
    }

    public static NeighborhoodModel createNeighborhoodModelWithId(Long id) {
        DepartmentModel department = new DepartmentModel(1L, "Departamento Test", "Desc Depto");
        CityModel city = new CityModel(1L, "Ciudad Test", "Desc Ciudad", department);
        return createNeighborhoodModel(id, "Barrio Test", "Descripción de barrio para pruebas", city);
    }

    public static NeighborhoodModel createNeighborhoodModelWithName(String name) {
        DepartmentModel department = new DepartmentModel(1L, "Departamento Test", "Desc Depto");
        CityModel city = new CityModel(1L, "Ciudad Test", "Desc Ciudad", department);
        return createNeighborhoodModel(1L, name, "Descripción de barrio para pruebas", city);
    }

    public static NeighborhoodModel createNeighborhoodModelWithDescription(String description) {
        DepartmentModel department = new DepartmentModel(1L, "Departamento Test", "Desc Depto");
        CityModel city = new CityModel(1L, "Ciudad Test", "Desc Ciudad", department);
        return createNeighborhoodModel(1L, "Barrio Test", description, city);
    }

    public static NeighborhoodModel createNeighborhoodModelWithCity(CityModel city) {
        return createNeighborhoodModel(1L, "Barrio Test", "Descripción de barrio para pruebas", city);
    }

    public static NeighborhoodModel createNeighborhoodModelWithEmptyName() {
        DepartmentModel department = new DepartmentModel(1L, "Departamento Test", "Desc Depto");
        CityModel city = new CityModel(1L, "Ciudad Test", "Desc Ciudad", department);
        return createNeighborhoodModel(1L, "", "Descripción de barrio para pruebas", city);
    }

    public static NeighborhoodModel createNeighborhoodModelWithEmptyDescription() {
        DepartmentModel department = new DepartmentModel(1L, "Departamento Test", "Desc Depto");
        CityModel city = new CityModel(1L, "Ciudad Test", "Desc Ciudad", department);
        return createNeighborhoodModel(1L, "Barrio Test", "", city);
    }

    public static NeighborhoodModel createNeighborhoodModelWithNullCity() {
        return createNeighborhoodModel(1L, "Barrio Test", "Descripción de barrio para pruebas", null);
    }
}
