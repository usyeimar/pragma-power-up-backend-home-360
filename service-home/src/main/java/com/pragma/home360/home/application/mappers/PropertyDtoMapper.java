package com.pragma.home360.home.application.mappers;

import com.pragma.home360.home.application.dto.request.SavePropertyRequest;
import com.pragma.home360.home.application.dto.request.filters.PropertyFilterRequest;
import com.pragma.home360.home.application.dto.response.PropertyResponse;
import com.pragma.home360.home.domain.model.PropertyFilterModel;
import com.pragma.home360.home.domain.model.PropertyModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {CategoryDtoMapper.class, LocationDtoMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PropertyDtoMapper {

    @Mappings({
            @Mapping(source = "categoryId", target = "category.id"),
            @Mapping(source = "locationId", target = "location.id")
    })
    PropertyModel fromRequestToModel(SavePropertyRequest request);

    PropertyFilterModel fromFilterRequestToModel(PropertyFilterRequest request);

    @Mappings({
            @Mapping(source = "category", target = "category"),
            @Mapping(source = "location", target = "location"),
            @Mapping(source = "images", target = "images"),
            @Mapping(source = "price", target = "price"),
            @Mapping(source = "createdAt", target = "createdAt"),
            @Mapping(source = "updatedAt", target = "updatedAt")
    })
    PropertyResponse fromModelToResponse(PropertyModel propertyModel);

}
