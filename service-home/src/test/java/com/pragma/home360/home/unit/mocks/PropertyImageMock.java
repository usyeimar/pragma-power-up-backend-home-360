package com.pragma.home360.home.unit.mocks;

import com.pragma.home360.home.domain.model.PropertyImageModel;

public class PropertyImageMock {

    public static PropertyImageModel createPropertyImageModel(Long id, String imageUrl, String description, Boolean isMainImage, Long propertyId) {
        PropertyImageModel model = new PropertyImageModel();
        model.setId(id);
        model.setImageUrl(imageUrl);
        model.setDescription(description);
        model.setIsMainImage(isMainImage);
        model.setPropertyId(propertyId);
        return model;
    }

    public static PropertyImageModel createDefaultPropertyImageModel() {
        return createPropertyImageModel(1L, "/media/properties/1/test-image.jpg", "Test Image Description", false, 1L);
    }

    public static PropertyImageModel createPropertyImageModelWithId(Long id) {
        PropertyImageModel model = createDefaultPropertyImageModel();
        model.setId(id);
        return model;
    }

    public static PropertyImageModel createPropertyImageModelWithPropertyId(Long propertyId) {
        PropertyImageModel model = createDefaultPropertyImageModel();
        model.setPropertyId(propertyId);
        return model;
    }

    public static PropertyImageModel createPropertyImageModelAsMain() {
        PropertyImageModel model = createDefaultPropertyImageModel();
        model.setIsMainImage(true);
        return model;
    }

    public static PropertyImageModel createPropertyImageModelWithNullUrl() {
        PropertyImageModel model = createDefaultPropertyImageModel();
        model.setImageUrl(null);
        return model;
    }
}