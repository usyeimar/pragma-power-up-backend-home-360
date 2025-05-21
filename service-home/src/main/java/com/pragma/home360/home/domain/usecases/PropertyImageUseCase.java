package com.pragma.home360.home.domain.usecases;

import com.pragma.home360.home.application.services.FileStorageService;
import com.pragma.home360.home.domain.exceptions.ModelNotFoundException;
import com.pragma.home360.home.domain.exceptions.ValidationException;
import com.pragma.home360.home.domain.model.PropertyImageModel;
import com.pragma.home360.home.domain.ports.in.PropertyImageServicePort;
import com.pragma.home360.home.domain.ports.out.PropertyImagePersistencePort;
import com.pragma.home360.home.domain.ports.out.PropertyPersistencePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.pragma.home360.home.domain.utils.constants.DomainConstants.*;

public class PropertyImageUseCase implements PropertyImageServicePort {

    private static final Logger log = LoggerFactory.getLogger(PropertyImageUseCase.class);
    private final PropertyImagePersistencePort propertyImagePersistencePort;
    private final PropertyPersistencePort propertyPersistencePort;
    private final FileStorageService fileStorageService;

    public PropertyImageUseCase(PropertyImagePersistencePort propertyImagePersistencePort,
                                PropertyPersistencePort propertyPersistencePort,
                                FileStorageService fileStorageService) {
        this.propertyImagePersistencePort = propertyImagePersistencePort;
        this.propertyPersistencePort = propertyPersistencePort;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public PropertyImageModel savePropertyImage(Long propertyId, MultipartFile imageFile, String description, boolean isMainImage) {
        if (!propertyPersistencePort.existsPropertyById(propertyId)) {
            throw new ModelNotFoundException(String.format(PROPERTY_IMAGE_PROPERTY_NOT_FOUND, propertyId));
        }
        if (imageFile == null || imageFile.isEmpty()) {
            throw new ValidationException(PROPERTY_IMAGE_FILE_EMPTY);
        }

        String imageUrl;
        try {
            imageUrl = fileStorageService.storeFile(imageFile, propertyId, imageFile.getOriginalFilename());
        } catch (IOException e) {
            throw new RuntimeException(String.format(PROPERTY_IMAGE_STORAGE_FAILED, e.getMessage()), e);
        }

        PropertyImageModel imageModel = new PropertyImageModel();
        imageModel.setPropertyId(propertyId);
        imageModel.setImageUrl(imageUrl);
        imageModel.setDescription(description);
        imageModel.setIsMainImage(isMainImage);

        if (isMainImage) {
            propertyImagePersistencePort.clearMainImageFlag(propertyId);
        }

        return propertyImagePersistencePort.save(imageModel);
    }

    @Override
    public List<PropertyImageModel> getPropertyImages(Long propertyId) {
        if (!propertyPersistencePort.existsPropertyById(propertyId)) {
            throw new ModelNotFoundException(String.format(PROPERTY_IMAGE_PROPERTY_NOT_FOUND, propertyId));
        }
        return propertyImagePersistencePort.findAllByPropertyId(propertyId);
    }

    @Override
    public PropertyImageModel getPropertyImageById(Long imageId) {
        return propertyImagePersistencePort.findById(imageId)
                .orElseThrow(() -> new ModelNotFoundException(String.format(PROPERTY_IMAGE_NOT_FOUND_BY_ID, imageId)));
    }

    @Override
    public void deletePropertyImage(Long imageId) {
        PropertyImageModel imageModel = propertyImagePersistencePort.findById(imageId)
                .orElseThrow(() -> new ModelNotFoundException(String.format(PROPERTY_IMAGE_NOT_FOUND_FOR_DELETE, imageId)));

        try {
            fileStorageService.deleteFile(imageModel.getImageUrl());
        } catch (IOException e) {
            log.error(PROPERTY_IMAGE_LOG_DELETE_FAILED, imageModel.getImageUrl(), e.getMessage(), e);
        }
        propertyImagePersistencePort.deleteById(imageId);
    }

    @Override
    public void setMainPropertyImage(Long propertyId, Long imageId) {
        if (!propertyPersistencePort.existsPropertyById(propertyId)) {
            throw new ModelNotFoundException(String.format(PROPERTY_IMAGE_PROPERTY_NOT_FOUND, propertyId));
        }
        PropertyImageModel imageToSetAsMain = propertyImagePersistencePort.findById(imageId)
                .orElseThrow(() -> new ModelNotFoundException(String.format(PROPERTY_IMAGE_NOT_FOUND_BY_ID, imageId)));

        if (!imageToSetAsMain.getPropertyId().equals(propertyId)) {
            throw new ValidationException(String.format(PROPERTY_IMAGE_DOES_NOT_BELONG_TO_PROPERTY, imageId, propertyId));
        }

        propertyImagePersistencePort.clearMainImageFlag(propertyId);
        imageToSetAsMain.setIsMainImage(true);
        propertyImagePersistencePort.save(imageToSetAsMain);
    }
}
