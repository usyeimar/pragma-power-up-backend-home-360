package com.pragma.home360.home.application.mappers;

import com.pragma.home360.home.application.dto.response.CategoryResponse;
import com.pragma.home360.home.domain.model.CategoryModel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE
)
public interface CategoryDtoMapper {
    CategoryModel toModel(com.pragma.home360.home.application.dto.request.SaveCategoryRequest request);
    CategoryResponse toResponse(CategoryModel categoryModel);
    List<CategoryResponse> toResponseList(List<CategoryModel> categories);
}
