package com.pragma.home360.home.application.mappers;

import com.pragma.home360.home.application.dto.request.SavePropertyRequest;
import com.pragma.home360.home.application.dto.response.PropertyResponse;
import com.pragma.home360.home.domain.model.PropertyModel;

public interface PropertyDtoMapper {

    PropertyModel fromRequestToModel(SavePropertyRequest request);

    PropertyResponse fromModelToResponse(PropertyModel propertyModel);




}
