package com.pragma.home360.home.domain.ports.in;

import com.pragma.home360.home.domain.model.CityModel;

import java.util.List;
import java.util.Optional;

public interface CityServicePort {
    CityModel saveCity(CityModel city);

    List<CityModel> getAllCities(int page, int size);

    Optional<CityModel> getCityById(Long id);

    long getCityCount();
}