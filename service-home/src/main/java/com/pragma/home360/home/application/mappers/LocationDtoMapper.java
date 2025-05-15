package com.pragma.home360.home.application.mappers;

import com.pragma.home360.home.application.dto.request.SaveLocationRequest;
import com.pragma.home360.home.application.dto.response.LocationResponse;
import com.pragma.home360.home.application.dto.response.LocationSearchResponse;
import com.pragma.home360.home.domain.model.LocationModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocationDtoMapper {

    @Mappings({
            @Mapping(source = "address", target = "address"),
            @Mapping(source = "latitude", target = "latitude"),
            @Mapping(source = "longitude", target = "longitude"),
            @Mapping(source = "referencePoint", target = "referencePoint"),
            @Mapping(source = "neighborhoodId", target = "neighborhoodId"),
            @Mapping(source = "cityId", target = "cityId"),
    })
    LocationModel toModel(SaveLocationRequest request);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "address", target = "address"),
            @Mapping(source = "latitude", target = "latitude"),
            @Mapping(source = "longitude", target = "longitude"),
            @Mapping(source = "referencePoint", target = "referencePoint"),
            @Mapping(source = "neighborhoodId", target = "neighborhoodId"),
            @Mapping(source = "cityId", target = "cityId"),
    })
    LocationResponse toResponse(LocationModel model);

    List<LocationResponse> toResponseList(List<LocationModel> models);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "address", target = "address"),
            @Mapping(source = "latitude", target = "latitude"),
            @Mapping(source = "longitude", target = "longitude"),
            @Mapping(source = "referencePoint", target = "referencePoint"),
            @Mapping(source = "neighborhoodName", target = "neighborhoodName"),
            @Mapping(source = "cityName", target = "cityName"),
            @Mapping(source = "departmentName", target = "departmentName"),
    })
    LocationSearchResponse modelToSearchResponse(LocationModel model);

    List<LocationSearchResponse> modelListToSearchResponseList(List<LocationModel> models);
}