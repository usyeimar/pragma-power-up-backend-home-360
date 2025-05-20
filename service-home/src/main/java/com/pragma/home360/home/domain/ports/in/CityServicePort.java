package com.pragma.home360.home.domain.ports.in;

import com.pragma.home360.home.domain.model.CityModel;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;

import java.util.List;
import java.util.Optional;

public interface CityServicePort {
    CityModel saveCity(CityModel city);

    PagedResult<CityModel> getAllCities(int page, int size);

    Optional<CityModel> getCityById(Long id);

    long getCityCount();
}