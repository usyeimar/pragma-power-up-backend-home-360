package com.pragma.home360.home.application.mappers;

import com.pragma.home360.home.application.dto.request.SaveNeighborhoodRequest;
import com.pragma.home360.home.application.dto.response.NeighborhoodResponse;
import com.pragma.home360.home.domain.model.NeighborhoodModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NeighborhoodDtoMapper {

    NeighborhoodModel toModel(SaveNeighborhoodRequest request);

    @Mapping(target = "city.id", source = "city.id")
    @Mapping(target = "city.name", source = "city.name")
    @Mapping(target = "city.description", source = "city.description")
    @Mapping(target = "city.department.id", source = "city.department.id")
    @Mapping(target = "city.department.name", source = "city.department.name")
    @Mapping(target = "city.department.description", source = "city.department.description")
    NeighborhoodResponse toResponse(NeighborhoodModel model);

    @Mapping(target = "city.id", source = "city.id")
    @Mapping(target = "city.name", source = "city.name")
    @Mapping(target = "city.description", source = "city.description")
    @Mapping(target = "city.department.id", source = "city.department.id")
    @Mapping(target = "city.department.name", source = "city.department.name")
    @Mapping(target = "city.department.description", source = "city.department.description")
    List<NeighborhoodResponse> fromModelListToResponseList(List<NeighborhoodModel> models);
}