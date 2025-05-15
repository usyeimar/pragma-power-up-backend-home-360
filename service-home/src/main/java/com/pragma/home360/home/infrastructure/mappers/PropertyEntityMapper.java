package com.pragma.home360.home.infrastructure.mappers;


import com.pragma.home360.home.domain.model.PropertyModel;
import com.pragma.home360.home.infrastructure.entities.PropertyEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PropertyEntityMapper {

    PropertyModel toModel(PropertyEntity propertyEntity);

    PropertyEntity toEntity(PropertyModel propertyModel);




}
