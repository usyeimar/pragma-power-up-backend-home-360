package com.pragma.home360.home.application.services.impl;


import com.pragma.home360.home.application.dto.request.PropertyImageUploadRequest;
import com.pragma.home360.home.application.dto.response.PropertyImageResponse;
import com.pragma.home360.home.application.mappers.PropertyImageDtoMapper;
import com.pragma.home360.home.application.services.PropertyImageService;
import com.pragma.home360.home.domain.model.PropertyImageModel;
import com.pragma.home360.home.domain.ports.in.PropertyImageServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyImageServiceImpl implements PropertyImageService {

    private final PropertyImageServicePort propertyImageServicePort;
    private final PropertyImageDtoMapper propertyImageDtoMapper;

    @Override
    @Transactional
    public PropertyImageResponse uploadImageToProperty(Long propertyId, PropertyImageUploadRequest request) {
        PropertyImageModel savedImageModel = propertyImageServicePort.savePropertyImage(
                propertyId,
                request.imageFile(),
                request.description(),
                request.isMainImage()
        );
        return propertyImageDtoMapper.toResponse(savedImageModel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropertyImageResponse> getImagesForProperty(Long propertyId) {
        List<PropertyImageModel> imageModels = propertyImageServicePort.getPropertyImages(propertyId);
        return imageModels.stream()
                .map(propertyImageDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PropertyImageResponse getImageById(Long imageId) {
        PropertyImageModel imageModel = propertyImageServicePort.getPropertyImageById(imageId);
        return propertyImageDtoMapper.toResponse(imageModel);
    }

    @Override
    @Transactional
    public void deleteImage(Long imageId) {
        propertyImageServicePort.deletePropertyImage(imageId);
    }

    @Override
    @Transactional
    public void setMainImage(Long propertyId, Long imageId) {
        propertyImageServicePort.setMainPropertyImage(propertyId, imageId);
    }
}