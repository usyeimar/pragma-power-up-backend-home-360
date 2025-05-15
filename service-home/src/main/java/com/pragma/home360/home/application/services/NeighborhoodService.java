package com.pragma.home360.home.application.services;

import com.pragma.home360.home.application.dto.response.PaginatedResponse;
import com.pragma.home360.home.application.dto.request.SaveNeighborhoodRequest;
import com.pragma.home360.home.application.dto.request.filters.FilterRequest;
import com.pragma.home360.home.application.dto.response.NeighborhoodResponse;

public interface NeighborhoodService {

    NeighborhoodResponse saveNeighborhood(SaveNeighborhoodRequest request);

    NeighborhoodResponse getNeighborhoodById(Long id);

    PaginatedResponse<NeighborhoodResponse> getAllNeighborhoods(FilterRequest filter);

    PaginatedResponse<NeighborhoodResponse> getNeighborhoodsByCity(Long cityId, FilterRequest filter);
}