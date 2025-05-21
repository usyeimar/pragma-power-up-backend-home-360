package com.pragma.home360.home.infrastructure.mappers;

import com.pragma.home360.home.domain.model.PropertyImageModel;
import com.pragma.home360.home.infrastructure.entities.PropertyImageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PropertyImageEntityMapper {

    @Mappings({
            @Mapping(source = "property.id", target = "propertyId")
    })
    PropertyImageModel toModel(PropertyImageEntity entity);

    @Mappings({
            @Mapping(source = "propertyId", target = "property.id")
    })
    PropertyImageEntity toEntity(PropertyImageModel model);

    List<PropertyImageModel> toModelList(List<PropertyImageEntity> entities);

    List<PropertyImageEntity> toEntityList(List<PropertyImageModel> models);
}