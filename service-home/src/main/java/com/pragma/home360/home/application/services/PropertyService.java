package com.pragma.home360.home.application.services;


import com.pragma.home360.home.application.dto.request.SavePropertyRequest;
import com.pragma.home360.home.application.dto.request.filters.PropertyFilterModel;
import com.pragma.home360.home.application.dto.response.PropertyResponse;
import com.pragma.home360.home.domain.model.PropertyModel;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;

public interface PropertyService {

    PropertyResponse saveProperty(SavePropertyRequest propertyRequest);

    PropertyModel getPropertyById(Long id);

    PagedResult<PropertyModel> getProperties(PropertyFilterModel propertyFilterModel);

    void updateProperty(Long id, PropertyModel propertyModel);

    void deleteProperty(Long id);

    boolean existsPropertyById(Long id);

    boolean existsPropertyByName(String name);
}
