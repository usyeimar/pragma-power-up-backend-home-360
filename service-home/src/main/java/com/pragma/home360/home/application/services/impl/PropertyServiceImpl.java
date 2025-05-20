package com.pragma.home360.home.application.services.impl;

import com.pragma.home360.home.application.dto.request.SavePropertyRequest;
import com.pragma.home360.home.application.dto.request.filters.PropertyFilterRequest;
import com.pragma.home360.home.application.dto.response.PropertyResponse;
import com.pragma.home360.home.application.mappers.PropertyDtoMapper;
import com.pragma.home360.home.application.services.PropertyService;
import com.pragma.home360.home.domain.model.PropertyFilterModel;
import com.pragma.home360.home.domain.model.PropertyModel;
import com.pragma.home360.home.domain.ports.in.PropertyServicePort;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyServicePort propertyServicePort;
    private final PropertyDtoMapper propertyDtoMapper;


    @Override
    public PropertyResponse saveProperty(SavePropertyRequest propertyRequest) {

        PropertyModel propertyModel = propertyDtoMapper.fromRequestToModel(propertyRequest);
        PropertyModel savedProperty = propertyServicePort.saveProperty(propertyModel);

        return propertyDtoMapper.fromModelToResponse(savedProperty);
    }

    @Override
    public PropertyModel getPropertyById(Long id) {
        return null;
    }

    @Override
    public PagedResult<PropertyModel> getAllProperties(PropertyFilterRequest propertyFilterRequest) {
        PropertyFilterModel propertyFilterModel = propertyDtoMapper.fromFilterRequestToModel(propertyFilterRequest);
        PagedResult<PropertyModel> pagedResult = propertyServicePort.getAllProperties(propertyFilterModel);
        return pagedResult;
    }

    @Override
    public void updateProperty(Long id, PropertyModel propertyModel) {

    }

    @Override
    public void deleteProperty(Long id) {

    }

    @Override
    public boolean existsPropertyById(Long id) {
        return false;
    }

    @Override
    public boolean existsPropertyByName(String name) {
        return false;
    }
}