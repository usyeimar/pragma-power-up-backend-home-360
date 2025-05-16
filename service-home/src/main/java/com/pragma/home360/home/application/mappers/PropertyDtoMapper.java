package com.pragma.home360.home.application.mappers;

import com.pragma.home360.home.application.dto.request.SavePropertyRequest;
import com.pragma.home360.home.application.dto.response.PropertyResponse;
import com.pragma.home360.home.domain.model.PropertyModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PropertyDtoMapper {

    PropertyModel fromRequestToModel(SavePropertyRequest request);

    PropertyResponse fromModelToResponse(PropertyModel propertyModel);


}
