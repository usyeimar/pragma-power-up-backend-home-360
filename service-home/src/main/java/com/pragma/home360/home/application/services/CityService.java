package com.pragma.home360.home.application.services;

import com.pragma.home360.home.application.dto.response.PaginatedResponse;
import com.pragma.home360.home.application.dto.request.filters.FilterRequest;
import com.pragma.home360.home.application.dto.request.SaveCityRequest;
import com.pragma.home360.home.application.dto.response.CityResponse;

public interface CityService {

    CityResponse saveCity(SaveCityRequest request);

    PaginatedResponse<CityResponse> getAllCities(FilterRequest paginationRequest);

    CityResponse getCityById(Long id);
}
