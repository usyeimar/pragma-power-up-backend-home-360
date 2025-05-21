package com.pragma.home360.home.infrastructure.mappers;

import com.pragma.home360.home.domain.model.PropertyModel;
import com.pragma.home360.home.infrastructure.entities.PropertyEntity;
import com.pragma.home360.home.infrastructure.entities.PropertyImageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {LocationEntityMapper.class, CategoryEntityMapper.class})
public interface PropertyEntityMapper {

    @Mappings({
            @Mapping(source = "location", target = "location"),
            @Mapping(source = "category", target = "category"),
            @Mapping(source = "images", target = "images", qualifiedByName = "propertyImageEntityListToStringList"),
            @Mapping(source = "price", target = "price"),
            @Mapping(source = "createdAt", target = "createdAt"),
            @Mapping(source = "updatedAt", target = "updatedAt")
    })
    PropertyModel toModel(PropertyEntity propertyEntity);

    @Mappings({

            @Mapping(source = "location.id", target = "location.id"),
            @Mapping(source = "category.id", target = "category.id"),
            @Mapping(target = "images", ignore = true),
            @Mapping(source = "price", target = "price"),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true)
    })
    PropertyEntity toEntity(PropertyModel propertyModel);

    List<PropertyModel> toModelList(List<PropertyEntity> propertyEntities);

    @Named("propertyImageEntityListToStringList")
    default List<String> propertyImageEntityListToStringList(List<PropertyImageEntity> images) {
        if (images == null) {
            return Collections.emptyList();
        }
        return images.stream()
                .map(PropertyImageEntity::getImageUrl)
                .collect(Collectors.toList());
    }
}
