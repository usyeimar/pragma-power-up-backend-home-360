package com.pragma.home360.home.domain.usecases;

import com.pragma.home360.home.application.dto.request.filters.PropertyFilterModel;
import com.pragma.home360.home.domain.model.PropertyModel;
import com.pragma.home360.home.domain.ports.in.PropertyServicePort;
import com.pragma.home360.home.domain.ports.out.CategoryPersistencePort;
import com.pragma.home360.home.domain.ports.out.CityPersistencePort;
import com.pragma.home360.home.domain.ports.out.LocationPersistencePort;
import com.pragma.home360.home.domain.ports.out.PropertyPersistencePort;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;

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
