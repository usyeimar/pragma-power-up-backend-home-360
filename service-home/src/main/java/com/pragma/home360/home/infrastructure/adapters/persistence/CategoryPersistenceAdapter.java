package com.pragma.home360.home.infrastructure.adapters.persistence;

import com.pragma.home360.home.domain.model.CategoryFilterModel;
import com.pragma.home360.home.domain.model.CategoryModel;
import com.pragma.home360.home.domain.ports.out.CategoryPersistencePort;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;
import com.pragma.home360.home.domain.utils.pagination.PaginationUtils;
import com.pragma.home360.home.infrastructure.entities.CategoryEntity;
import com.pragma.home360.home.infrastructure.mappers.CategoryEntityMapper;
import com.pragma.home360.home.infrastructure.repositories.mysql.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryPersistenceAdapter implements CategoryPersistencePort {

    private final CategoryRepository categoryRepository;
    private final CategoryEntityMapper categoryEntityMapper;

    @Override
    public CategoryModel save(CategoryModel categoryModel) {
        CategoryEntity categoryEntity = categoryEntityMapper.toEntity(categoryModel);
        CategoryEntity savedCategory = categoryRepository.save(categoryEntity);
        return categoryEntityMapper.toModel(savedCategory);
    }

    @Override
    public Optional<CategoryModel> getCategoryByName(String categoryName) {
        return categoryRepository.findByName(categoryName)
                .map(categoryEntityMapper::toModel);
    }

    @Override
    public PagedResult<CategoryModel> getCategories(CategoryFilterModel categoryFilterModel) {
        var pageable = PaginationUtils.createPageable(
                categoryFilterModel.page(),
                categoryFilterModel.size(),
                categoryFilterModel.sortField(),
                categoryFilterModel.direction()
        );

        var page = categoryRepository.findAll(pageable);
        return PaginationUtils.toPagedResult(page.map(categoryEntityMapper::toModel));
    }

    @Override
    public Optional<CategoryModel> getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryEntityMapper::toModel);
    }
}
