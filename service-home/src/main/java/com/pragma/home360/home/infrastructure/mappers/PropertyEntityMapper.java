package com.pragma.home360.home.infrastructure.mappers;


import com.pragma.home360.home.domain.model.NeighborhoodModel;
import com.pragma.home360.home.domain.model.PropertyModel;
import com.pragma.home360.home.infrastructure.entities.NeighborHoodEntity;
import com.pragma.home360.home.infrastructure.entities.PropertyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PropertyEntityMapper {

    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "category.id", target = "categoryId")
    PropertyModel toModel(PropertyEntity propertyEntity);

    @Mapping(source = "locationId", target = "location.id")
    @Mapping(source = "categoryId", target = "category.id")
    PropertyEntity toEntity(PropertyModel propertyModel);


//    @Mapping(source = "city.id", target = "cityId")
//    NeighborhoodModel toModel(NeighborHoodEntity entity);
//
//    @Mapping(source = "cityId", target = "city.id")
//    @Mapping(target = "locations", ignore = true)
//    NeighborHoodEntity toEntity(NeighborhoodModel model);


}
