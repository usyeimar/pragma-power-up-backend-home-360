package com.pragma.home360.home.infrastructure.mappers;

import com.pragma.home360.home.domain.model.CityModel;
import com.pragma.home360.home.infrastructure.entities.CityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CityEntityMapper {

    @Mappings({
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "department.id", target = "department.id"),
            @Mapping(source = "department.name", target = "department.name"),
            @Mapping(source = "department.description", target = "department.description"),
    })
    CityModel toModel(CityEntity entity);

    @Mappings({
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "departmentId", target = "department.id")
    })
    CityEntity toEntity(CityModel model);


    List<CityModel> toModelList(List<CityEntity> entities);

}
