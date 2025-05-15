package com.pragma.home360.home.infrastructure.mappers;

import com.pragma.home360.home.domain.model.NeighborhoodModel;
import com.pragma.home360.home.infrastructure.entities.NeighborHoodEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NeighborhoodEntityMapper {

    @Mapping(source = "city.id", target = "cityId")
    NeighborhoodModel toModel(NeighborHoodEntity entity);

    @Mapping(source = "cityId", target = "city.id")
    @Mapping(target = "locations", ignore = true)
    NeighborHoodEntity toEntity(NeighborhoodModel model);

    List<NeighborhoodModel> toModelList(List<NeighborHoodEntity> entities);
}