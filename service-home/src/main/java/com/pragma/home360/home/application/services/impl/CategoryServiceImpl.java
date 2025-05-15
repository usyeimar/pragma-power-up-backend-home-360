package com.pragma.home360.home.application.services.impl;

import com.pragma.home360.home.application.dto.request.filters.CategoryFilterRequest;
import com.pragma.home360.home.application.dto.request.SaveCategoryRequest;
import com.pragma.home360.home.application.dto.response.CategoryResponse;
import com.pragma.home360.home.application.dto.response.PaginatedResponse;
import com.pragma.home360.home.application.mappers.CategoryDtoMapper;
import com.pragma.home360.home.application.services.CategoryService;
import com.pragma.home360.home.domain.model.CategoryFilterModel;
import com.pragma.home360.home.domain.model.CategoryModel;
import com.pragma.home360.home.domain.ports.in.CategoryServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryServicePort categoryServicePort;
    private final CategoryDtoMapper categoryDtoMapper;

    @Override
    public CategoryResponse save(SaveCategoryRequest request) {
        CategoryModel categoryModel = categoryDtoMapper.toModel(request);
        CategoryModel savedCategory = categoryServicePort.saveCategory(categoryModel);
        return categoryDtoMapper.toResponse(savedCategory);
    }

    @Override
    public PaginatedResponse<CategoryResponse> getCategories(CategoryFilterRequest request) {

        var categoryPage = categoryServicePort.getAllCategories(new CategoryFilterModel(
                request.page(),
                request.size(),
                request.sortField(),
                request.direction()
        ));

        List<CategoryResponse> categories = categoryPage.content()
                .stream()
                .map(categoryDtoMapper::toResponse)
                .toList();

        return new PaginatedResponse<>(
                categories,
                categoryPage.page(),
                categoryPage.size(),
                categoryPage.totalPages(),
                categoryPage.totalElements()
        );
    }

}
