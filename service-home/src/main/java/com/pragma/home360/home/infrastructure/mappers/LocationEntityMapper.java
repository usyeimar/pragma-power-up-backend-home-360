package com.pragma.home360.home.infrastructure.mappers;

import com.pragma.home360.home.domain.model.LocationModel;
import com.pragma.home360.home.infrastructure.entities.LocationEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocationEntityMapper {

    @Mapping(source = "neighborhood.id", target = "neighborhoodId")
    @Mapping(source = "neighborhood.name", target = "neighborhoodName")
    @Mapping(source = "neighborhood.city.name", target = "cityName")
    @Mapping(source = "neighborhood.city.department.id", target = "departmentId")
    @Mapping(source = "neighborhood.city.department.name", target = "departmentName")
    @Mapping(source = "city.id", target = "cityId")
    LocationModel toModel(LocationEntity entity);

    @Mapping(target = "neighborhood.id", source = "neighborhoodId")
    @Mapping(target = "city.id", source = "cityId")
    LocationEntity toEntity(LocationModel model);

    List<LocationModel> toModelList(List<LocationEntity> entities);
}