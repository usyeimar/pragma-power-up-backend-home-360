package com.pragma.home360.home.infrastructure.adapters.persistence;

import com.pragma.home360.home.domain.model.PropertyImageModel;
import com.pragma.home360.home.domain.ports.out.PropertyImagePersistencePort;
import com.pragma.home360.home.infrastructure.entities.PropertyEntity;
import com.pragma.home360.home.infrastructure.entities.PropertyImageEntity;
import com.pragma.home360.home.infrastructure.mappers.PropertyImageEntityMapper;
import com.pragma.home360.home.infrastructure.repositories.mysql.PropertyImageRepository;
import com.pragma.home360.home.infrastructure.repositories.mysql.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PropertyImagePersistenceAdapter implements PropertyImagePersistencePort {

    private final PropertyImageRepository propertyImageRepository;
    private final PropertyRepository propertyRepository;
    private final PropertyImageEntityMapper mapper;

    @Override
    @Transactional
    public PropertyImageModel save(PropertyImageModel propertyImageModel) {
        PropertyImageEntity entity = mapper.toEntity(propertyImageModel);


        PropertyEntity property = propertyRepository.findById(propertyImageModel.getPropertyId())
                .orElseThrow(() -> new com.pragma.home360.home.domain.exceptions.ModelNotFoundException("Property not found with id: " + propertyImageModel.getPropertyId()));
        entity.setProperty(property);

        if (Boolean.TRUE.equals(entity.getIsMainImage())) {
            propertyImageRepository.clearMainImageFlagForProperty(entity.getProperty().getId());
        }

        PropertyImageEntity savedEntity = propertyImageRepository.save(entity);
        return mapper.toModel(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PropertyImageModel> findById(Long id) {
        return propertyImageRepository.findById(id).map(mapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropertyImageModel> findAllByPropertyId(Long propertyId) {
        return propertyImageRepository.findAllByPropertyId(propertyId)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        propertyImageRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return propertyImageRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PropertyImageModel> findMainImageByPropertyId(Long propertyId) {
        return propertyImageRepository.findByPropertyIdAndIsMainImageTrue(propertyId)
                .map(mapper::toModel);
    }

    @Override
    @Transactional
    public void clearMainImageFlag(Long propertyId) {
        propertyImageRepository.clearMainImageFlagForProperty(propertyId);
    }
}