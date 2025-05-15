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

    @Mapping(target = "cityName", ignore = true)
    NeighborhoodResponse toResponse(NeighborhoodModel model);

    List<NeighborhoodResponse> fromModelListToResponseList(List<NeighborhoodModel> models);
}