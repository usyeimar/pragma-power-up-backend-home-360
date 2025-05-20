package com.pragma.home360.home.application.services.impl;

import com.pragma.home360.home.application.dto.response.PaginatedResponse;
import com.pragma.home360.home.application.dto.request.LocationSearchRequest;
import com.pragma.home360.home.application.dto.request.SaveLocationRequest;
import com.pragma.home360.home.application.dto.response.LocationResponse;
import com.pragma.home360.home.application.dto.response.LocationSearchResponse;
import com.pragma.home360.home.application.mappers.LocationDtoMapper;
import com.pragma.home360.home.application.services.LocationService;
import com.pragma.home360.home.domain.model.LocationModel;
import com.pragma.home360.home.domain.ports.in.LocationServicePort;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationServicePort locationServicePort;
    private final LocationDtoMapper locationDtoMapper;

    @Override
    public LocationResponse saveLocation(SaveLocationRequest request) {
        LocationModel locationModel = locationDtoMapper.toModel(request);
        LocationModel savedLocation = locationServicePort.saveLocation(locationModel);
        return locationDtoMapper.toResponse(savedLocation);
    }

    @Override
    public Optional<LocationResponse> getLocationById(Long id) {
        return locationServicePort.getLocationById(id)
                .map(locationDtoMapper::toResponse);
    }

    @Override
    public PaginatedResponse<LocationSearchResponse> searchLocations(LocationSearchRequest request) {
        String searchText = request.searchText() != null ? request.searchText().trim() : "";
        int page = request.page();
        int size = request.size();
        String sortBy = request.sortBy();
        String sortDirection = request.direction().toString();

        PagedResult<LocationModel> resultPage = locationServicePort.searchLocations(searchText, page, size, sortBy, sortDirection);

        List<LocationSearchResponse> locations = resultPage
                .content().stream()
                .map(locationDtoMapper::toSearchResponse)
                .collect(Collectors.toList());


        return new PaginatedResponse<>(locations, resultPage.page(), resultPage.size(), resultPage.totalPages(), resultPage.totalElements());
    }


}