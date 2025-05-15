package com.pragma.home360.home.domain.ports.out;

import com.pragma.home360.home.domain.model.CategoryFilterModel;
import com.pragma.home360.home.domain.model.CategoryModel;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;

import java.util.Optional;

public interface CategoryPersistencePort {
    CategoryModel save(CategoryModel categoryModel);

    Optional<CategoryModel> getCategoryByName(String categoryName);

    PagedResult<CategoryModel> getCategories(CategoryFilterModel categoryFilterModel);

}
