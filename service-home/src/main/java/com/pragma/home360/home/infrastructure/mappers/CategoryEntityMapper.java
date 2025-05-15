package com.pragma.home360.home.infrastructure.mappers;

import com.pragma.home360.home.domain.model.CategoryModel;
import com.pragma.home360.home.infrastructure.entities.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryEntityMapper {

    @Mapping(target = "createdAt", ignore = true)
    CategoryEntity toEntity(CategoryModel model);

    CategoryModel toModel(CategoryEntity entity);

    List<CategoryModel> entityListToModelList(List<CategoryEntity> categories);

}
