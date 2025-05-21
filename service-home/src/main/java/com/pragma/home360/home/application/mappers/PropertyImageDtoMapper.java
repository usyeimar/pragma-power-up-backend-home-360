package com.pragma.home360.home.application.mappers;

import com.pragma.home360.home.application.dto.response.PropertyImageResponse;
import com.pragma.home360.home.domain.model.PropertyImageModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PropertyImageDtoMapper {

    @Mapping(source = "propertyId", target = "propertyId")
    PropertyImageResponse toResponse(PropertyImageModel model);

    List<PropertyImageResponse> toResponseList(List<PropertyImageModel> models);
}