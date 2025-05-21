package com.pragma.home360.home.domain.ports.in;

import com.pragma.home360.home.domain.model.PropertyImageModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PropertyImageServicePort {
    PropertyImageModel savePropertyImage(Long propertyId, MultipartFile imageFile, String description, boolean isMainImage);

    List<PropertyImageModel> getPropertyImages(Long propertyId);

    PropertyImageModel getPropertyImageById(Long imageId);

    void deletePropertyImage(Long imageId);

    void setMainPropertyImage(Long propertyId, Long imageId);
}