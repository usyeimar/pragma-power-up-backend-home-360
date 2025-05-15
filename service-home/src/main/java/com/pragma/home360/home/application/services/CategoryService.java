package com.pragma.home360.home.application.services;

import com.pragma.home360.home.application.dto.request.filters.CategoryFilterRequest;
import com.pragma.home360.home.application.dto.request.SaveCategoryRequest;
import com.pragma.home360.home.application.dto.response.CategoryResponse;
import com.pragma.home360.home.application.dto.response.PaginatedResponse;


public interface CategoryService {
    CategoryResponse save(SaveCategoryRequest request);

    PaginatedResponse<CategoryResponse> getCategories(CategoryFilterRequest request);
}
