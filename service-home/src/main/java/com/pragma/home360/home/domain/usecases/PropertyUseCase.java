package com.pragma.home360.home.domain.usecases;

import com.pragma.home360.home.domain.model.PropertyFilterModel;
import com.pragma.home360.home.domain.model.PropertyModel;
import com.pragma.home360.home.domain.ports.in.PropertyServicePort;
import com.pragma.home360.home.domain.ports.out.CategoryPersistencePort;
import com.pragma.home360.home.domain.ports.out.LocationPersistencePort;
import com.pragma.home360.home.domain.ports.out.PropertyPersistencePort;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;

import java.text.Normalizer;

import static com.pragma.home360.home.domain.utils.constants.DomainConstants.*;
import static com.pragma.home360.home.domain.utils.constants.DomainConstants.MAX_PAGE_SIZE;
import static com.pragma.home360.home.domain.utils.constants.DomainConstants.MAX_PAGINATION_OFFSET;
import static com.pragma.home360.home.domain.utils.constants.DomainConstants.PAGINATION_MAX_OFFSET;
import static com.pragma.home360.home.domain.utils.constants.DomainConstants.PAGINATION_SIZE_BETWEEN;
import static com.pragma.home360.home.domain.utils.constants.Validator.validateCustom;

public class PropertyUseCase implements PropertyServicePort {

    private final PropertyPersistencePort propertyPersistencePort;
    private final LocationPersistencePort locationPersistencePort;
    private final CategoryPersistencePort categoryPersistencePort;

    public PropertyUseCase(PropertyPersistencePort propertyPersistencePort, LocationPersistencePort locationPersistencePort, CategoryPersistencePort categoryPersistencePort) {
        this.propertyPersistencePort = propertyPersistencePort;
        this.locationPersistencePort = locationPersistencePort;
        this.categoryPersistencePort = categoryPersistencePort;

    }


    @Override
    public PropertyModel saveProperty(PropertyModel propertyModel) {
        return propertyPersistencePort.saveProperty(propertyModel);
    }

    @Override
    public PropertyModel getPropertyById(Long id) {
        return null;
    }

    @Override
    public PagedResult<PropertyModel> getAllProperties(PropertyFilterModel propertyFilterModel) {

        String searchText = propertyFilterModel.searchTerm();
        String normalizedText = searchText != null ? normalizeText(searchText) : "";
        int page = propertyFilterModel.page();
        int size = propertyFilterModel.size();

        validateCustom(p -> p >= INITIAL_PAGE, page, PAGINATION_PAGE_NEGATIVE, RuntimeException.class.getName());
        validateCustom(s -> s >= MIN_PAGE_SIZE && s <= MAX_PAGE_SIZE, size, PAGINATION_SIZE_BETWEEN, null);
        validateCustom(p -> (long) p * size <= MAX_PAGINATION_OFFSET, page, PAGINATION_MAX_OFFSET, null);
        return propertyPersistencePort.getAllProperties(normalizedText, page, size);

    }

    @Override
    public void updateProperty(Long id, PropertyModel propertyModel) {

    }

    @Override
    public void deleteProperty(Long id) {

    }

    @Override
    public boolean existsPropertyById(Long id) {
        return false;
    }

    @Override
    public boolean existsPropertyByName(String name) {
        return false;
    }

    private String normalizeText(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        return Normalizer.normalize(text.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}", "");
    }
}
