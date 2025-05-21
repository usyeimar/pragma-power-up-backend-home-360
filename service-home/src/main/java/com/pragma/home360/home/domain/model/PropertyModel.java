package com.pragma.home360.home.domain.model;

import com.pragma.home360.home.infrastructure.entities.enums.PropertyPublicationStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PropertyModel {
    private Long id;
    private String name;
    private String description;
    private Integer numberOfRooms;
    private Integer numberOfBathrooms;
    private BigDecimal price;
    private LocationModel location;
    private CategoryModel category;
    private LocalDate activePublicationDate;
    private PropertyPublicationStatus publicationStatus;
    private List<String> images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PropertyModel() {
    }

    public PropertyModel(Long id, String name, String description, Integer numberOfRooms, Integer numberOfBathrooms, BigDecimal price, LocationModel location, CategoryModel category, LocalDate activePublicationDate, PropertyPublicationStatus publicationStatus, List<String> images, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.numberOfRooms = numberOfRooms;
        this.numberOfBathrooms = numberOfBathrooms;
        this.price = price;
        this.location = location;
        this.category = category;
        this.activePublicationDate = activePublicationDate;
        this.publicationStatus = publicationStatus;
        this.images = images;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(Integer numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public Integer getNumberOfBathrooms() {
        return numberOfBathrooms;
    }

    public void setNumberOfBathrooms(Integer numberOfBathrooms) {
        this.numberOfBathrooms = numberOfBathrooms;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocationModel getLocation() {
        return location;
    }

    public void setLocation(LocationModel location) {
        this.location = location;
    }

    public CategoryModel getCategory() {
        return category;
    }

    public void setCategory(CategoryModel category) {
        this.category = category;
    }

    public LocalDate getActivePublicationDate() {
        return activePublicationDate;
    }

    public void setActivePublicationDate(LocalDate activePublicationDate) {
        this.activePublicationDate = activePublicationDate;
    }

    public PropertyPublicationStatus getPublicationStatus() {
        return publicationStatus;
    }

    public void setPublicationStatus(PropertyPublicationStatus publicationStatus) {
        this.publicationStatus = publicationStatus;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "PropertyModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", numberOfRooms=" + numberOfRooms +
                ", numberOfBathrooms=" + numberOfBathrooms +
                ", price=" + price +
                ", locationId=" + (location != null ? location.getId() : "null") +
                ", categoryId=" + (category != null ? category.getId() : "null") +
                ", activePublicationDate=" + activePublicationDate +
                ", publicationStatus=" + publicationStatus +
                ", images=" + images +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
