package com.pragma.home360.home.domain.ports.out;

import com.pragma.home360.home.domain.model.PropertyFilterModel;
import com.pragma.home360.home.domain.model.PropertyModel;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;
import com.pragma.home360.home.infrastructure.entities.enums.PropertyPublicationStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PropertyPersistencePort {

    PropertyModel saveProperty(PropertyModel propertyModel);

    Optional<PropertyModel> getPropertyById(Long id); // Cambiado para devolver Optional

    PagedResult<PropertyModel> getAllProperties(PropertyFilterModel propertyFilterModel);


    void updateProperty(Long id, PropertyModel propertyModel);

    void deleteProperty(Long id);

    boolean existsPropertyById(Long id);

    boolean existsPropertyByName(String name);


    List<PropertyModel> findByPublicationStatusAndActiveDateLessThanEqual(PropertyPublicationStatus status, LocalDate date);

    void updateProperties(List<PropertyModel> propertyModels);

}
