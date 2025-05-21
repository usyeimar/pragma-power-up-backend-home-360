package com.pragma.home360.home.application.services.impl;

import com.pragma.home360.home.application.dto.request.SavePropertyRequest;
import com.pragma.home360.home.application.dto.request.filters.PropertyFilterRequest;
import com.pragma.home360.home.application.dto.response.PaginatedResponse;
import com.pragma.home360.home.application.dto.response.PropertyResponse;
import com.pragma.home360.home.application.mappers.PropertyDtoMapper;
import com.pragma.home360.home.application.services.PropertyService;
import com.pragma.home360.home.domain.model.PropertyFilterModel;
import com.pragma.home360.home.domain.model.PropertyModel;
import com.pragma.home360.home.domain.ports.in.PropertyServicePort;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importar para transacciones

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyServicePort propertyServicePort;
    private final PropertyDtoMapper propertyDtoMapper;

    @Override
    @Transactional
    public PropertyResponse saveProperty(SavePropertyRequest propertyRequest) {
        PropertyModel propertyModel = propertyDtoMapper.fromRequestToModel(propertyRequest);
        PropertyModel savedProperty = propertyServicePort.saveProperty(propertyModel);
        return propertyDtoMapper.fromModelToResponse(savedProperty);
    }

    @Override
    @Transactional(readOnly = true)
    public PropertyResponse getPropertyById(Long id) {
        PropertyModel propertyModel = propertyServicePort.getPropertyById(id);
        if (propertyModel == null) {
            return null;
        }
        return propertyDtoMapper.fromModelToResponse(propertyModel);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<PropertyResponse> getAllProperties(PropertyFilterRequest propertyFilterRequest) {
        PropertyFilterModel propertyFilterModel = propertyDtoMapper.fromFilterRequestToModel(propertyFilterRequest);
        PagedResult<PropertyModel> pagedResultModel = propertyServicePort.getAllProperties(propertyFilterModel);

        List<PropertyResponse> propertyResponses = pagedResultModel.content().stream()
                .map(propertyDtoMapper::fromModelToResponse)
                .collect(Collectors.toList());

        return new PaginatedResponse<>(
                propertyResponses,
                pagedResultModel.page(),
                pagedResultModel.size(),
                pagedResultModel.totalPages(),
                pagedResultModel.totalElements()
        );
    }
}
