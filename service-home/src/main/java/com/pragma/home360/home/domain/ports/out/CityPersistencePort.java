package com.pragma.home360.home.domain.ports.out;

import com.pragma.home360.home.domain.model.CityModel;
import com.pragma.home360.home.domain.model.FilterModel;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;

import java.util.List;
import java.util.Optional;

public interface CityPersistencePort {

    CityModel saveCity(CityModel cityModel);

    Optional<CityModel> getCityById(Long id);

    Optional<CityModel> getCityByName(String name);

    PagedResult<CityModel> getAllCities(int page, int size);

    long getCityCount();

    boolean existsCityByName(String name);

    PagedResult<CityModel> getAllCities(FilterModel filter);

}