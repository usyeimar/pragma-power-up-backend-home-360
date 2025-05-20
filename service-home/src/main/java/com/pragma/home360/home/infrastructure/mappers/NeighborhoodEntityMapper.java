package com.pragma.home360.home.infrastructure.mappers;

import com.pragma.home360.home.domain.model.NeighborhoodModel;
import com.pragma.home360.home.infrastructure.entities.NeighborHoodEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NeighborhoodEntityMapper {

    @Mapping(source = "city.id", target = "city.id")
    @Mapping(source = "city.name", target = "city.name")
    @Mapping(source = "city.description", target = "city.description")
    @Mapping(source = "city.department.id", target = "city.department.id")
    @Mapping(source = "city.department.name", target = "city.department.name")
    @Mapping(source = "city.department.description", target = "city.department.description")
    NeighborhoodModel toModel(NeighborHoodEntity entity);

    @Mapping(source = "city.id", target = "city.id")
    @Mapping(target = "locations", ignore = true)
    NeighborHoodEntity toEntity(NeighborhoodModel model);

    List<NeighborhoodModel> toModelList(List<NeighborHoodEntity> entities);
}