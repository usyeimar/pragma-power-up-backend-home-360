package com.pragma.home360.home.domain.model;

import com.pragma.home360.home.infrastructure.entities.enums.PropertyPublicationStatus;

import java.time.LocalDate;

public class PropertyModel {
    private Long id;
    private String name;
    private String description;
    private Integer numberOfRooms;
    private Integer numberOfBathrooms;
    private Double price;
    private LocationModel location;
    private CategoryModel category;
    private LocalDate activePublicationDate;
    private PropertyPublicationStatus publicationStatus;

    public PropertyModel() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getNumberOfRooms() {
        return numberOfRooms;
    }

    public Integer getNumberOfBathrooms() {
        return numberOfBathrooms;
    }

    public Double getPrice() {
        return price;
    }

    public LocationModel getLocation() {
        return location;
    }

    public CategoryModel getCategory() {
        return category;
    }

    public LocalDate getActivePublicationDate() {
        return activePublicationDate;
    }

    public PropertyPublicationStatus getPublicationStatus() {
        return publicationStatus;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNumberOfRooms(Integer numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public void setNumberOfBathrooms(Integer numberOfBathrooms) {
        this.numberOfBathrooms = numberOfBathrooms;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setLocation(LocationModel location) {
        this.location = location;
    }

    public void setCategory(CategoryModel category) {
        this.category = category;
    }

    public void setActivePublicationDate(LocalDate activePublicationDate) {
        this.activePublicationDate = activePublicationDate;
    }

    public void setPublicationStatus(PropertyPublicationStatus publicationStatus) {
        this.publicationStatus = publicationStatus;
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
                ", location=" + location +
                ", category=" + category +
                ", activePublicationDate=" + activePublicationDate +
                ", publicationStatus=" + publicationStatus +
                '}';
    }
}
