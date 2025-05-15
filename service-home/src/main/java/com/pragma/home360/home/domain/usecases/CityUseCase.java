package com.pragma.home360.home.domain.usecases;

import com.pragma.home360.home.domain.exceptions.ModelNotFoundException;
import com.pragma.home360.home.domain.model.CityModel;
import com.pragma.home360.home.domain.ports.in.CityServicePort;
import com.pragma.home360.home.domain.ports.out.CityPersistencePort;
import com.pragma.home360.home.domain.ports.out.DepartmentPersistencePort;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.pragma.home360.home.domain.utils.constants.Validator.validateMaxLength;
import static com.pragma.home360.home.domain.utils.constants.Validator.validateCustom;
import static com.pragma.home360.home.domain.utils.constants.Validator.validateNotEmpty;
import static com.pragma.home360.home.domain.utils.constants.DomainConstants.*;

public class CityUseCase implements CityServicePort {

    private final CityPersistencePort cityPersistencePort;
    private final DepartmentPersistencePort departmentPersistencePort;

    public CityUseCase(CityPersistencePort cityPersistencePort, DepartmentPersistencePort departmentPersistencePort) {
        this.cityPersistencePort = cityPersistencePort;
        this.departmentPersistencePort = departmentPersistencePort;
    }

    @Override
    public CityModel saveCity(CityModel city) {

        validateNotEmpty(city.getName(), CITY_NAME_CANNOT_BE_EMPTY);
        validateMaxLength(city.getName(), CITY_NAME_MAX_LENGTH, CITY_NAME_MAX_LENGTH_EXCEEDED);
        validateCustom(
                (String name) -> !cityPersistencePort.existsCityByName(name.trim()),
                city.getName(),
                String.format(CITY_NAME_ALREADY_EXISTS, city.getName()),
                ModelNotFoundException.class.getName()
        );

        if (city.getDescription() != null) {
            validateMaxLength(city.getDescription(), CITY_DESCRIPTION_MAX_LENGTH, CITY_DESCRIPTION_MAX_LENGTH_EXCEEDED);
        }


        validateCustom(Objects::nonNull, city.getDepartmentId(), DEPARTMENT_ID_CANNOT_BE_NULL, null);
        validateCustom(
                departmentId -> departmentPersistencePort
                        .getDepartmentById(departmentId)
                        .isPresent(),
                city.getDepartmentId(),
                DEPARTMENT_NOT_FOUND,
                ModelNotFoundException.class.getName()
        );

        return cityPersistencePort.saveCity(city);
    }

    @Override
    public Optional<CityModel> getCityById(Long id) {
        return cityPersistencePort.getCityById(id);
    }

    @Override
    public List<CityModel> getAllCities(int page, int size) {
        DepartmentUseCase.validateFilters(page, size);
        return cityPersistencePort.getAllCities(page, size);
    }

    @Override
    public long getCityCount() {
        return cityPersistencePort.getCityCount();
    }
}