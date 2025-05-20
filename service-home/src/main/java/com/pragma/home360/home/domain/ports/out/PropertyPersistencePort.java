package com.pragma.home360.home.domain.ports.out;

import com.pragma.home360.home.application.dto.request.filters.PropertyFilterRequest;
import com.pragma.home360.home.domain.model.PropertyModel;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;


public interface PropertyPersistencePort {

    PropertyModel saveProperty(PropertyModel propertyModel);

    PropertyModel getPropertyById(Long id);

    PagedResult<PropertyModel> getAllProperties(PropertyFilterRequest propertyFilterModel);

    void updateProperty(Long id, PropertyModel propertyModel);

    void deleteProperty(Long id);

    boolean existsPropertyById(Long id);

    boolean existsPropertyByName(String name);
}
