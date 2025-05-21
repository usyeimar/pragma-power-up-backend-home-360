package com.pragma.home360.home.application.services;

import com.pragma.home360.home.application.dto.request.PropertyImageUploadRequest;
import com.pragma.home360.home.application.dto.response.PropertyImageResponse;

import java.util.List;

public interface PropertyImageService {
    PropertyImageResponse uploadImageToProperty(Long propertyId, PropertyImageUploadRequest request);
    List<PropertyImageResponse> getImagesForProperty(Long propertyId);
    PropertyImageResponse getImageById(Long imageId);
    void deleteImage(Long imageId);
    void setMainImage(Long propertyId, Long imageId);
}