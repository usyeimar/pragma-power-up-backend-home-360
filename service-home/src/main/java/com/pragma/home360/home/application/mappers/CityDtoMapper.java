package com.pragma.home360.home.application.mappers;

import com.pragma.home360.home.application.dto.request.SaveCityRequest;
import com.pragma.home360.home.application.dto.response.CityResponse;
import com.pragma.home360.home.domain.model.CityModel;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CityDtoMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "department.id", target = "department.id"),
            @Mapping(source = "department.name", target = "department.name"),
            @Mapping(source = "department.description", target = "department.description")
    })
    CityResponse modelToResponse(CityModel cityModel);


    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "departmentId", target = "departmentId")
    })
    List<CityResponse> toResponseList(List<CityModel> cityModelList);

    @Mappings({
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "departmentId", target = "departmentId")
    })
    CityModel requestToModel(SaveCityRequest saveCityRequest);


}
