package com.pragma.home360.home.unit.mocks;

import com.pragma.home360.home.domain.model.CategoryModel;
import com.pragma.home360.home.domain.model.LocationModel;
import com.pragma.home360.home.domain.model.PropertyModel;
import com.pragma.home360.home.infrastructure.entities.enums.PropertyPublicationStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PropertyMock {

    public static PropertyModel createPropertyModel(Long id, String name, String description,
                                                    Integer numberOfRooms, Integer numberOfBathrooms,
                                                    BigDecimal price, Long locationId, Long categoryId,
                                                    LocalDate activePublicationDate,
                                                    PropertyPublicationStatus status, List<String> images,
                                                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        PropertyModel model = new PropertyModel();
        model.setId(id);
        model.setName(name);
        model.setDescription(description);
        model.setNumberOfRooms(numberOfRooms);
        model.setNumberOfBathrooms(numberOfBathrooms);
        model.setPrice(price);
        if (locationId != null) {
            model.setLocation(new LocationModel()); // Asumiendo constructor por defecto
            model.getLocation().setId(locationId);
        }
        if (categoryId != null) {
            model.setCategory(new CategoryModel()); // Asumiendo constructor por defecto
            model.getCategory().setId(categoryId);
        }
        model.setActivePublicationDate(activePublicationDate);
        model.setPublicationStatus(status);
        model.setImages(images != null ? images : new ArrayList<>());
        model.setCreatedAt(createdAt);
        model.setUpdatedAt(updatedAt);
        return model;
    }

    public static PropertyModel createDefaultPropertyModel() {
        return createPropertyModel(
                1L,
                "Beautiful House",
                "A very nice house with a garden.",
                3,
                2,
                new BigDecimal("250000.00"),
                1L, // locationId
                1L, // categoryId
                LocalDate.now().plusDays(5),
                PropertyPublicationStatus.PUBLICATION_PENDING,
                List.of("image1.jpg", "image2.jpg"),
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().minusDays(1)
        );
    }

    public static PropertyModel createPropertyModelWithId(Long id) {
        PropertyModel model = createDefaultPropertyModel();
        model.setId(id);
        return model;
    }

    public static PropertyModel createPropertyModelWithName(String name) {
        PropertyModel model = createDefaultPropertyModel();
        model.setName(name);
        return model;
    }

    public static PropertyModel createPropertyModelWithEmptyName() {
        PropertyModel model = createDefaultPropertyModel();
        model.setName("");
        return model;
    }

    public static PropertyModel createPropertyModelWithNullName() {
        PropertyModel model = createDefaultPropertyModel();
        model.setName(null);
        return model;
    }

    public static PropertyModel createPropertyModelWithPrice(BigDecimal price) {
        PropertyModel model = createDefaultPropertyModel();
        model.setPrice(price);
        return model;
    }

    public static PropertyModel createPropertyModelWithNegativePrice() {
        PropertyModel model = createDefaultPropertyModel();
        model.setPrice(new BigDecimal("-100.00"));
        return model;
    }

    public static PropertyModel createPropertyModelWithZeroPrice() {
        PropertyModel model = createDefaultPropertyModel();
        model.setPrice(BigDecimal.ZERO);
        return model;
    }

    public static PropertyModel createPropertyModelWithRooms(Integer rooms) {
        PropertyModel model = createDefaultPropertyModel();
        model.setNumberOfRooms(rooms);
        return model;
    }

    public static PropertyModel createPropertyModelWithZeroRooms() {
        PropertyModel model = createDefaultPropertyModel();
        model.setNumberOfRooms(0);
        return model;
    }

    public static PropertyModel createPropertyModelWithBathrooms(Integer bathrooms) {
        PropertyModel model = createDefaultPropertyModel();
        model.setNumberOfBathrooms(bathrooms);
        return model;
    }

    public static PropertyModel createPropertyModelWithZeroBathrooms() {
        PropertyModel model = createDefaultPropertyModel();
        model.setNumberOfBathrooms(0);
        return model;
    }

    public static PropertyModel createPropertyModelWithActiveDate(LocalDate activeDate) {
        PropertyModel model = createDefaultPropertyModel();
        model.setActivePublicationDate(activeDate);
        return model;
    }

    public static PropertyModel createPropertyModelWithPastActiveDate() {
        PropertyModel model = createDefaultPropertyModel();
        model.setActivePublicationDate(LocalDate.now().minusDays(1));
        return model;
    }

    public static PropertyModel createPropertyModelWithFutureActiveDateExceedsLimit() {
        PropertyModel model = createDefaultPropertyModel();
        model.setActivePublicationDate(LocalDate.now().plusMonths(2));
        return model;
    }

    public static PropertyModel createPropertyModelWithNullActiveDate() {
        PropertyModel model = createDefaultPropertyModel();
        model.setActivePublicationDate(null);
        return model;
    }

    public static PropertyModel createPropertyModelWithCategoryId(Long categoryId) {
        PropertyModel model = createDefaultPropertyModel();
        if (categoryId == null) {
            model.setCategory(null);
        } else {
            CategoryModel category = new CategoryModel();
            category.setId(categoryId);
            model.setCategory(category);
        }
        return model;
    }

    public static PropertyModel createPropertyModelWithNullCategoryId() {
        PropertyModel model = createDefaultPropertyModel();
        model.setCategory(new CategoryModel()); // CategoryModel with null id
        return model;
    }

    public static PropertyModel createPropertyModelWithNullCategoryObject() {
        PropertyModel model = createDefaultPropertyModel();
        model.setCategory(null);
        return model;
    }

    public static PropertyModel createPropertyModelWithLocationId(Long locationId) {
        PropertyModel model = createDefaultPropertyModel();
        if (locationId == null) {
            model.setLocation(null);
        } else {
            LocationModel location = new LocationModel();
            location.setId(locationId);
            model.setLocation(location);
        }
        return model;
    }

    public static PropertyModel createPropertyModelWithNullLocationId() {
        PropertyModel model = createDefaultPropertyModel();
        model.setLocation(new LocationModel()); // LocationModel with null id
        return model;
    }

    public static PropertyModel createPropertyModelWithNullLocationObject() {
        PropertyModel model = createDefaultPropertyModel();
        model.setLocation(null);
        return model;
    }

    public static PropertyModel createPropertyModelWithStatus(PropertyPublicationStatus status) {
        PropertyModel model = createDefaultPropertyModel();
        model.setPublicationStatus(status);
        return model;
    }
}