package com.pragma.home360.home.infrastructure.adapters.persistence;

import com.pragma.home360.home.application.dto.request.filters.PropertyFilterModel;
import com.pragma.home360.home.domain.model.PropertyModel;
import com.pragma.home360.home.domain.ports.out.PropertyPersistencePort;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class PropertyPersistenceAdapter implements PropertyPersistencePort {
    @Override
    public PropertyModel saveProperty(PropertyModel propertyModel) {
        return null;
    }

    @Override
    public PropertyModel getPropertyById(Long id) {
        return null;
    }

    @Override
    public PagedResult<PropertyModel> getAllProperties(PropertyFilterModel propertyFilterModel) {
        return null;
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
}
