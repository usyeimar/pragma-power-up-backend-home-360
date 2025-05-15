package com.pragma.home360.home.domain.usecases;

import com.pragma.home360.home.domain.exceptions.AlreadyExistsException;
import com.pragma.home360.home.domain.model.CategoryFilterModel;
import com.pragma.home360.home.domain.model.CategoryModel;
import com.pragma.home360.home.domain.ports.in.CategoryServicePort;
import com.pragma.home360.home.domain.ports.out.CategoryPersistencePort;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;

import java.util.Objects;
import java.util.Optional;

import static com.pragma.home360.home.domain.utils.constants.DomainConstants.*;
import static com.pragma.home360.home.domain.utils.constants.Validator.validateMaxLength;
import static com.pragma.home360.home.domain.utils.constants.Validator.validateCustom;
import static com.pragma.home360.home.domain.utils.constants.Validator.validateNotEmpty;


public class CategoryUseCase implements CategoryServicePort {

    private final CategoryPersistencePort categoryPersistencePort;

    public CategoryUseCase(CategoryPersistencePort categoryPersistencePort) {
        this.categoryPersistencePort = categoryPersistencePort;
    }

    @Override
    public CategoryModel saveCategory(CategoryModel categoryModel) {

        validateNotEmpty(categoryModel.getName(), CATEGORY_NAME_CANNOT_BE_EMPTY);
        validateNotEmpty(categoryModel.getDescription(), CATEGORY_DESCRIPTION_CANNOT_BE_EMPTY);
        validateMaxLength(categoryModel.getName(), CATEGORY_NAME_MAX_LENGTH, CATEGORY_NAME_MAX_LENGTH_EXCEEDED);
        validateMaxLength(categoryModel.getDescription(), CATEGORY_DESCRIPTION_MAX_LENGTH, CATEGORY_DESCRIPTION_MAX_LENGTH_EXCEEDED);

        Optional<CategoryModel> category = categoryPersistencePort.getCategoryByName(categoryModel.getName().trim());
        if (category.isPresent()) {
            throw new AlreadyExistsException(CATEGORY_ALREADY_EXISTS_EXCEPTION_MESSAGE);
        }

        return categoryPersistencePort.save(categoryModel);

    }


    @Override
    public PagedResult<CategoryModel> getAllCategories(CategoryFilterModel filter) {

        validateCustom(p -> p >= INITIAL_PAGE, filter.page(), PAGINATION_PAGE_NEGATIVE, null);
        validateCustom(s -> s >= MIN_PAGE_SIZE && s <= MAX_PAGE_SIZE, filter.size(), PAGINATION_SIZE_BETWEEN, null);
        validateCustom(p -> (long) p * filter.size() <= MAX_PAGINATION_OFFSET, filter.page(), PAGINATION_MAX_OFFSET, null);
        validateCustom(sf -> sf != null && !sf.isBlank(), filter.sortField(), SORT_FIELD_INVALID, null);
        validateCustom(CATEGORY_ALLOWED_SORT_FIELDS::contains, filter.sortField(), SORT_FIELD_INVALID, null);
        validateCustom(Objects::nonNull, filter.direction(), SORT_DIRECTION_NULL, null);

        return categoryPersistencePort.getCategories(filter);
    }
}
