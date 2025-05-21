package com.pragma.home360.home.domain.usecases;

import com.pragma.home360.home.domain.exceptions.ModelNotFoundException;
import com.pragma.home360.home.domain.exceptions.ValidationException;
import com.pragma.home360.home.domain.model.PropertyFilterModel;
import com.pragma.home360.home.domain.model.PropertyModel;
import com.pragma.home360.home.domain.ports.in.PropertyServicePort;
import com.pragma.home360.home.domain.ports.out.CategoryPersistencePort;
import com.pragma.home360.home.domain.ports.out.LocationPersistencePort;
import com.pragma.home360.home.domain.ports.out.PropertyPersistencePort;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;
import com.pragma.home360.home.infrastructure.entities.enums.PropertyPublicationStatus;

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.pragma.home360.home.domain.utils.constants.DomainConstants.*;
import static com.pragma.home360.home.domain.utils.constants.Validator.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyUseCase implements PropertyServicePort {

    private static final Logger log = LoggerFactory.getLogger(PropertyUseCase.class);
    private final PropertyPersistencePort propertyPersistencePort;
    private final LocationPersistencePort locationPersistencePort;
    private final CategoryPersistencePort categoryPersistencePort;

    public PropertyUseCase(PropertyPersistencePort propertyPersistencePort, LocationPersistencePort locationPersistencePort, CategoryPersistencePort categoryPersistencePort) {
        this.propertyPersistencePort = propertyPersistencePort;
        this.locationPersistencePort = locationPersistencePort;
        this.categoryPersistencePort = categoryPersistencePort;
    }

    @Override
    public PropertyModel saveProperty(PropertyModel propertyModel) {
        validateNotEmpty(propertyModel.getName(), PROPERTY_NAME_CANNOT_BE_EMPTY);
        validateMaxLength(propertyModel.getName(), PROPERTY_NAME_MAX_LENGTH, String.format(PROPERTY_NAME_MAX_LENGTH_EXCEEDED, PROPERTY_NAME_MAX_LENGTH));

        if (propertyModel.getPrice() == null || propertyModel.getPrice().signum() <= 0) {
            throw new ValidationException(PROPERTY_PRICE_MUST_BE_POSITIVE);
        }
        if (propertyModel.getNumberOfRooms() == null || propertyModel.getNumberOfRooms() <= 0) {
            throw new ValidationException(PROPERTY_ROOMS_MUST_BE_POSITIVE);
        }
        if (propertyModel.getNumberOfBathrooms() == null || propertyModel.getNumberOfBathrooms() <= 0) {
            throw new ValidationException(PROPERTY_BATHROOMS_MUST_BE_POSITIVE);
        }

        LocalDate today = LocalDate.now();
        LocalDate activeDate = propertyModel.getActivePublicationDate();


        if (activeDate.isBefore(today)) {
            throw new ValidationException(PROPERTY_ACTIVE_DATE_IN_PAST);
        }
        if (activeDate.isAfter(today.plusMonths(1))) {
            throw new ValidationException(PROPERTY_ACTIVE_DATE_EXCEEDS_LIMIT);
        }

        if (activeDate.isAfter(today)) {
            propertyModel.setPublicationStatus(PropertyPublicationStatus.PUBLICATION_PENDING);
        } else {
            propertyModel.setPublicationStatus(PropertyPublicationStatus.PUBLISHED);
        }

        if (propertyModel.getCategory() == null || propertyModel.getCategory().getId() == null) {
            throw new ValidationException(PROPERTY_CATEGORY_ID_REQUIRED);
        }
        categoryPersistencePort.getCategoryById(propertyModel.getCategory().getId())
                .orElseThrow(() -> new ModelNotFoundException(String.format(CATEGORY_NOT_FOUND_BY_ID, propertyModel.getCategory().getId())));

        if (propertyModel.getLocation() == null || propertyModel.getLocation().getId() == null) {
            throw new ValidationException(PROPERTY_LOCATION_ID_REQUIRED);
        }
        locationPersistencePort.getLocationById(propertyModel.getLocation().getId())
                .orElseThrow(() -> new ModelNotFoundException(String.format(LOCATION_NOT_FOUND_BY_ID, propertyModel.getLocation().getId())));

        return propertyPersistencePort.saveProperty(propertyModel);
    }

    @Override
    public PropertyModel getPropertyById(Long id) {
        PropertyModel propertyModel = propertyPersistencePort.getPropertyById(id)
                .orElse(null);
        return propertyModel;
    }

    @Override
    public PagedResult<PropertyModel> getAllProperties(PropertyFilterModel propertyFilterModel) {
        validateCustom(p -> p >= INITIAL_PAGE, propertyFilterModel.page(), PAGINATION_PAGE_NEGATIVE, ValidationException.class.getName());
        validateCustom(s -> s >= MIN_PAGE_SIZE && s <= MAX_PAGE_SIZE, propertyFilterModel.size(), PAGINATION_SIZE_BETWEEN, ValidationException.class.getName());
        validateCustom(p -> (long) p * propertyFilterModel.size() <= MAX_PAGINATION_OFFSET, propertyFilterModel.page(), PAGINATION_MAX_OFFSET, ValidationException.class.getName());

        String searchTerm = propertyFilterModel.searchTerm();
        String normalizedSearchTerm = (searchTerm != null && !searchTerm.isBlank()) ? normalizeText(searchTerm) : null;

        PropertyFilterModel validatedFilter = new PropertyFilterModel(
                propertyFilterModel.page(),
                propertyFilterModel.size(),
                propertyFilterModel.sortField(),
                propertyFilterModel.direction(),
                normalizedSearchTerm
        );
        return propertyPersistencePort.getAllProperties(validatedFilter);
    }

    @Override
    public void updateProperty(Long id, PropertyModel propertyModel) {
        PropertyModel existingProperty = propertyPersistencePort.getPropertyById(id)
                .orElseThrow(() -> new ModelNotFoundException(String.format(PROPERTY_NOT_FOUND_FOR_UPDATE, id)));

        validateNotEmpty(propertyModel.getName(), PROPERTY_NAME_CANNOT_BE_EMPTY);

        LocalDate today = LocalDate.now();
        LocalDate activeDate = propertyModel.getActivePublicationDate();

        if (activeDate == null) {
            throw new ValidationException(PROPERTY_ACTIVE_DATE_REQUIRED);
        }

        if (existingProperty.getPublicationStatus() == PropertyPublicationStatus.PUBLISHED && activeDate.isBefore(today)) {
            if (!activeDate.isEqual(existingProperty.getActivePublicationDate())) {
                throw new ValidationException(PROPERTY_CANNOT_CHANGE_ACTIVE_DATE_TO_PAST);
            }
        } else if (activeDate.isBefore(today)) {
            throw new ValidationException(PROPERTY_ACTIVE_DATE_IN_PAST);
        }

        if (activeDate.isAfter(today.plusMonths(1))) {
            throw new ValidationException(PROPERTY_ACTIVE_DATE_EXCEEDS_LIMIT);
        }

        if (propertyModel.getPublicationStatus() == null ||
                propertyModel.getPublicationStatus() == PropertyPublicationStatus.PUBLISHED ||
                propertyModel.getPublicationStatus() == PropertyPublicationStatus.PUBLICATION_PENDING) {

            if (activeDate.isAfter(today)) {
                propertyModel.setPublicationStatus(PropertyPublicationStatus.PUBLICATION_PENDING);
            } else {
                propertyModel.setPublicationStatus(PropertyPublicationStatus.PUBLISHED);
            }
        }

        if (propertyModel.getCategory() != null && propertyModel.getCategory().getId() != null) {
            if (existingProperty.getCategory() == null || !propertyModel.getCategory().getId().equals(existingProperty.getCategory().getId())) {
                categoryPersistencePort.getCategoryById(propertyModel.getCategory().getId())
                        .orElseThrow(() -> new ModelNotFoundException(String.format(CATEGORY_NOT_FOUND_BY_ID, propertyModel.getCategory().getId())));
            }
        }
        if (propertyModel.getLocation() != null && propertyModel.getLocation().getId() != null) {
            if (existingProperty.getLocation() == null || !propertyModel.getLocation().getId().equals(existingProperty.getLocation().getId())) {
                locationPersistencePort.getLocationById(propertyModel.getLocation().getId())
                        .orElseThrow(() -> new ModelNotFoundException(String.format(LOCATION_NOT_FOUND_BY_ID, propertyModel.getLocation().getId())));
            }
        }

        propertyModel.setId(id);
        propertyModel.setCreatedAt(existingProperty.getCreatedAt());

        propertyPersistencePort.updateProperty(id, propertyModel);
    }

    @Override
    public void deleteProperty(Long id) {
        if (!propertyPersistencePort.existsPropertyById(id)) {
            throw new ModelNotFoundException(String.format(PROPERTY_NOT_FOUND_FOR_DELETE, id));
        }
        propertyPersistencePort.deleteProperty(id);
    }

    @Override
    public boolean existsPropertyById(Long id) {
        return propertyPersistencePort.existsPropertyById(id);
    }

    @Override
    public void processPendingPropertiesToPublish() {
        LocalDate today = LocalDate.now();
        log.info(PROPERTY_LOG_PROCESSING_PENDING, today);

        List<PropertyModel> propertiesToPublish = propertyPersistencePort.findByPublicationStatusAndActiveDateLessThanEqual(
                PropertyPublicationStatus.PUBLICATION_PENDING,
                today
        );

        if (propertiesToPublish.isEmpty()) {
            log.info(PROPERTY_LOG_NO_PENDING_TO_UPDATE);
            return;
        }

        log.info(PROPERTY_LOG_FOUND_TO_UPDATE, propertiesToPublish.size());

        List<PropertyModel> updatedProperties = propertiesToPublish.stream()
                .peek(property -> {
                    log.debug(PROPERTY_LOG_UPDATING_TO_PUBLISHED, property.getId());
                    property.setPublicationStatus(PropertyPublicationStatus.PUBLISHED);
                })
                .collect(Collectors.toList());

        propertyPersistencePort.updateProperties(updatedProperties);
        log.info(PROPERTY_LOG_UPDATED_COUNT, updatedProperties.size());
    }

    private String normalizeText(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        return Normalizer.normalize(text.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
