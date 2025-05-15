package com.pragma.home360.home.infrastructure.mappers;

import com.pragma.home360.home.domain.model.CityModel;
import com.pragma.home360.home.infrastructure.entities.CityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CityEntityMapper {

    @Mappings({
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "department.id", target = "departmentId")
    })
    CityModel toModel(CityEntity entity);

    @Mappings({
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "departmentId", target = "department.id")
    })
    CityEntity toEntity(CityModel model);
}
