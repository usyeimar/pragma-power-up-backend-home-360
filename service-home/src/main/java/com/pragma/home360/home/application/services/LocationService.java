package com.pragma.home360.home.application.services;

import com.pragma.home360.home.application.dto.response.PaginatedResponse;
import com.pragma.home360.home.application.dto.request.LocationSearchRequest;
import com.pragma.home360.home.application.dto.request.SaveLocationRequest;
import com.pragma.home360.home.application.dto.response.LocationResponse;
import com.pragma.home360.home.application.dto.response.LocationSearchResponse;

import java.util.Optional;

public interface LocationService {


    LocationResponse saveLocation(SaveLocationRequest request);

    Optional<LocationResponse> getLocationById(Long id);

    PaginatedResponse<LocationSearchResponse> searchLocations(LocationSearchRequest request);
}