package com.pragma.home360.home.application.services;

import com.pragma.home360.home.application.dto.request.SavePropertyRequest;
import com.pragma.home360.home.application.dto.request.filters.PropertyFilterRequest;
import com.pragma.home360.home.application.dto.response.PaginatedResponse;
import com.pragma.home360.home.application.dto.response.PropertyResponse;

public interface PropertyService {

    PropertyResponse saveProperty(SavePropertyRequest propertyRequest);

    PropertyResponse getPropertyById(Long id);

    PaginatedResponse<PropertyResponse> getAllProperties(PropertyFilterRequest propertyFilterRequest);

}
