package com.pragma.home360.home.domain.model;

public class PropertyImageModel {
    private Long id;
    private String imageUrl;
    private String description;
    private Boolean isMainImage;
    private Long propertyId;

    public PropertyImageModel() {
    }

    public PropertyImageModel(Long id, String imageUrl, String description, Boolean isMainImage, Long propertyId) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.description = description;
        this.isMainImage = isMainImage;
        this.propertyId = propertyId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsMainImage() {
        return isMainImage;
    }

    public void setIsMainImage(Boolean mainImage) {
        isMainImage = mainImage;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }
}