package com.pragma.home360.home.infrastructure.mappers;

import com.pragma.home360.home.domain.model.LocationModel;
import com.pragma.home360.home.infrastructure.entities.LocationEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocationEntityMapper {

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
    LocationModel toModel(LocationEntity entity);

    @Mapping(target = "neighborhood.id", source = "neighborhood.id")
    @Mapping(target = "city.id", source = "city.id")
    LocationEntity toEntity(LocationModel model);

    List<LocationModel> toModelList(List<LocationEntity> entities);
}