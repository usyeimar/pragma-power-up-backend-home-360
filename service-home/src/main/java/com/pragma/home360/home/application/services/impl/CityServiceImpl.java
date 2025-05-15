package com.pragma.home360.home.application.services.impl;

import com.pragma.home360.home.application.dto.response.PaginatedResponse;
import com.pragma.home360.home.application.dto.request.filters.FilterRequest;
import com.pragma.home360.home.application.dto.request.SaveCityRequest;
import com.pragma.home360.home.application.dto.response.CityResponse;
import com.pragma.home360.home.application.mappers.CityDtoMapper;
import com.pragma.home360.home.application.services.CityService;
import com.pragma.home360.home.domain.model.CityModel;
import com.pragma.home360.home.domain.ports.in.CityServicePort;

import java.util.List;

public class CityServiceImpl implements CityService {

    private final CityServicePort cityServicePort;
    private final CityDtoMapper cityDtoMapper;


    public CityServiceImpl(CityServicePort cityServicePort, CityDtoMapper cityDtoMapper) {
        this.cityServicePort = cityServicePort;
        this.cityDtoMapper = cityDtoMapper;
    }

    @Override
    public CityResponse saveCity(SaveCityRequest request) {
        CityModel cityModel = cityDtoMapper.requestToModel(request);
        CityModel savedCity = cityServicePort.saveCity(cityModel);
        return cityDtoMapper.modelToResponse(savedCity);
    }

    @Override
    public PaginatedResponse<CityResponse> getAllCities(FilterRequest paginationRequest) {
        int page = paginationRequest.page();
        int size = paginationRequest.size();

        List<CityModel> cities = cityServicePort.getAllCities(page, size);
        long totalCount = cityServicePort.getCityCount();

        List<CityResponse> cityResponses = cityDtoMapper.toResponseList(cities);

        int totalPages = size > 0 ? (int) Math.ceil((double) totalCount / size) : 0;

        return new PaginatedResponse<>(cityResponses, page, size, totalPages, totalCount);

    }

    @Override
    public CityResponse getCityById(Long id) {
        CityModel cityModel = cityServicePort.getCityById(id)
                .isPresent()
                ? cityServicePort.getCityById(id).get()
                : null;
        return cityDtoMapper.modelToResponse(cityModel);
    }
}