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
            @Mapping(source = "neighborhoodId", target = "neighborhood.id"),
            @Mapping(source = "cityId", target = "city.id"),
    })
    LocationModel toModel(SaveLocationRequest request);

    @Mappings({
            @Mapping(source = "address", target = "address"),
            @Mapping(source = "latitude", target = "latitude"),
            @Mapping(source = "longitude", target = "longitude"),
            @Mapping(source = "referencePoint", target = "referencePoint"),

            @Mapping(source = "neighborhood.id", target = "neighborhood.id"),
            @Mapping(source = "neighborhood.name", target = "neighborhood.name"),
            @Mapping(source = "neighborhood.description", target = "neighborhood.description"),

            @Mapping(source = "neighborhood.city.department.id", target = "neighborhood.city.department.id"),
            @Mapping(source = "neighborhood.city.department.name", target = "neighborhood.city.department.name"),
            @Mapping(source = "neighborhood.city.department.description", target = "neighborhood.city.department.description"),

            @Mapping(source = "neighborhood.city.id", target = "neighborhood.city.id"),
            @Mapping(source = "neighborhood.city.name", target = "neighborhood.city.name"),
            @Mapping(source = "neighborhood.city.description", target = "neighborhood.city.description"),

            @Mapping(source = "city.id", target = "city.id"),
            @Mapping(source = "city.name", target = "city.name"),
            @Mapping(source = "city.description", target = "city.description"),
    })
    LocationResponse toResponse(LocationModel model);


    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "address", target = "address"),
            @Mapping(source = "latitude", target = "latitude"),
            @Mapping(source = "longitude", target = "longitude"),
            @Mapping(source = "referencePoint", target = "referencePoint"),

            @Mapping(source = "neighborhood.id", target = "neighborhood.id"),
            @Mapping(source = "neighborhood.name", target = "neighborhood.name"),
            @Mapping(source = "neighborhood.description", target = "neighborhood.description"),
            @Mapping(source = "neighborhood.city.department.id", target = "neighborhood.city.department.id"),
            @Mapping(source = "neighborhood.city.department.name", target = "neighborhood.city.department.name"),
            @Mapping(source = "neighborhood.city.department.description", target = "neighborhood.city.department.description"),

            @Mapping(source = "city.id", target = "city.id"),
            @Mapping(source = "city.name", target = "city.name"),
            @Mapping(source = "city.description", target = "city.description")
    })
    LocationSearchResponse toSearchResponse(LocationModel model);

}