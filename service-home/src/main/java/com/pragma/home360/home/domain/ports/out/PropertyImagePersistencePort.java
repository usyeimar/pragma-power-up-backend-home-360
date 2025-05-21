package com.pragma.home360.home.domain.ports.out;

import com.pragma.home360.home.domain.model.PropertyImageModel;

import java.util.List;
import java.util.Optional;

public interface PropertyImagePersistencePort {
    PropertyImageModel save(PropertyImageModel propertyImageModel);

    Optional<PropertyImageModel> findById(Long id);

    List<PropertyImageModel> findAllByPropertyId(Long propertyId);

    void deleteById(Long id);

    boolean existsById(Long id);

    Optional<PropertyImageModel> findMainImageByPropertyId(Long propertyId);

    void clearMainImageFlag(Long propertyId);
}
