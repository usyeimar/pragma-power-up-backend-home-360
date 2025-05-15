package com.pragma.home360.home.domain.ports.in;

import com.pragma.home360.home.domain.model.CategoryFilterModel;
import com.pragma.home360.home.domain.model.CategoryModel;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;


public interface CategoryServicePort {
    CategoryModel saveCategory(CategoryModel categoryModel);

    PagedResult<CategoryModel> getAllCategories(CategoryFilterModel paginationModel);
}
