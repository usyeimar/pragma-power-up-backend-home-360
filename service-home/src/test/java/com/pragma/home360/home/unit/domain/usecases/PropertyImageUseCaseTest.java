package com.pragma.home360.home.unit.domain.usecases;

import com.pragma.home360.home.application.services.FileStorageService;
import com.pragma.home360.home.domain.exceptions.ModelNotFoundException;
import com.pragma.home360.home.domain.exceptions.ValidationException;
import com.pragma.home360.home.domain.model.PropertyImageModel;
import com.pragma.home360.home.domain.ports.out.PropertyImagePersistencePort;
import com.pragma.home360.home.domain.ports.out.PropertyPersistencePort;
import com.pragma.home360.home.domain.usecases.PropertyImageUseCase;
import com.pragma.home360.home.domain.utils.constants.DomainConstants;
import com.pragma.home360.home.unit.mocks.PropertyImageMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropertyImageUseCaseTest {

    @Mock
    private PropertyImagePersistencePort propertyImagePersistencePort;

    @Mock
    private PropertyPersistencePort propertyPersistencePort;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private PropertyImageUseCase propertyImageUseCase;

    @Captor
    private ArgumentCaptor<PropertyImageModel> propertyImageModelCaptor;
    @Captor
    private ArgumentCaptor<Long> longCaptor;

    private PropertyImageModel validPropertyImage;
    private MultipartFile mockImageFile;
    private final Long propertyId = 1L;
    private final Long imageId = 1L;

    @BeforeEach
    void setUp() {
        validPropertyImage = PropertyImageMock.createDefaultPropertyImageModel();
        mockImageFile = new MockMultipartFile("imageFile", "test-image.jpg", "image/jpeg", "test image content".getBytes());
    }

    @Nested
    @DisplayName("Tests para savePropertyImage")
    class SavePropertyImageTests {

        @Test
        @DisplayName("Debería guardar la imagen correctamente")
        void savePropertyImage_ValidData_ShouldSaveImage() throws IOException {
            when(propertyPersistencePort.existsPropertyById(propertyId)).thenReturn(true);
            when(fileStorageService.storeFile(any(MultipartFile.class), eq(propertyId), anyString())).thenReturn("/media/properties/1/saved-image.jpg");
            when(propertyImagePersistencePort.save(any(PropertyImageModel.class))).thenReturn(validPropertyImage);

            PropertyImageModel result = propertyImageUseCase.savePropertyImage(propertyId, mockImageFile, "description", false);

            assertNotNull(result);
            assertEquals(validPropertyImage.getImageUrl(), result.getImageUrl());
            verify(propertyImagePersistencePort).save(propertyImageModelCaptor.capture());
            assertEquals(propertyId, propertyImageModelCaptor.getValue().getPropertyId());
            assertEquals("/media/properties/1/saved-image.jpg", propertyImageModelCaptor.getValue().getImageUrl());
        }

        @Test
        @DisplayName("Debería limpiar la bandera de imagen principal si isMainImage es true")
        void savePropertyImage_IsMainImageTrue_ShouldClearPreviousMainFlag() throws IOException {
            when(propertyPersistencePort.existsPropertyById(propertyId)).thenReturn(true);
            when(fileStorageService.storeFile(any(MultipartFile.class), eq(propertyId), anyString())).thenReturn("/media/properties/1/main-image.jpg");
            when(propertyImagePersistencePort.save(any(PropertyImageModel.class))).thenReturn(validPropertyImage);

            propertyImageUseCase.savePropertyImage(propertyId, mockImageFile, "main image", true);

            verify(propertyImagePersistencePort).clearMainImageFlag(propertyId);
            verify(propertyImagePersistencePort).save(propertyImageModelCaptor.capture());
            assertTrue(propertyImageModelCaptor.getValue().getIsMainImage());
        }

        @Test
        @DisplayName("Debería lanzar ModelNotFoundException si la propiedad no existe")
        void savePropertyImage_PropertyNotFound_ShouldThrowModelNotFoundException() {
            when(propertyPersistencePort.existsPropertyById(propertyId)).thenReturn(false);

            ModelNotFoundException exception = assertThrows(ModelNotFoundException.class, () -> propertyImageUseCase.savePropertyImage(propertyId, mockImageFile, "desc", false));
            assertEquals(String.format(DomainConstants.PROPERTY_IMAGE_PROPERTY_NOT_FOUND, propertyId), exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar ValidationException si el archivo está vacío")
        void savePropertyImage_EmptyFile_ShouldThrowValidationException() {
            when(propertyPersistencePort.existsPropertyById(propertyId)).thenReturn(true);
            MultipartFile emptyFile = new MockMultipartFile("empty", new byte[0]);

            ValidationException exception = assertThrows(ValidationException.class, () -> propertyImageUseCase.savePropertyImage(propertyId, emptyFile, "desc", false));
            assertEquals(DomainConstants.PROPERTY_IMAGE_FILE_EMPTY, exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar RuntimeException si falla el almacenamiento del archivo")
        void savePropertyImage_StorageFails_ShouldThrowRuntimeException() throws IOException {
            when(propertyPersistencePort.existsPropertyById(propertyId)).thenReturn(true);
            when(fileStorageService.storeFile(any(MultipartFile.class), anyLong(), anyString())).thenThrow(new IOException("Storage error"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> propertyImageUseCase.savePropertyImage(propertyId, mockImageFile, "desc", false));
            assertTrue(exception.getMessage().contains(String.format(DomainConstants.PROPERTY_IMAGE_STORAGE_FAILED, "Storage error")));
        }
    }

    @Nested
    @DisplayName("Tests para getPropertyImages")
    class GetPropertyImagesTests {

        @Test
        @DisplayName("Debería retornar la lista de imágenes de la propiedad")
        void getPropertyImages_PropertyExists_ShouldReturnImageList() {
            when(propertyPersistencePort.existsPropertyById(propertyId)).thenReturn(true);
            when(propertyImagePersistencePort.findAllByPropertyId(propertyId)).thenReturn(Collections.singletonList(validPropertyImage));

            List<PropertyImageModel> result = propertyImageUseCase.getPropertyImages(propertyId);

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(validPropertyImage.getId(), result.get(0).getId());
        }

        @Test
        @DisplayName("Debería lanzar ModelNotFoundException si la propiedad no existe")
        void getPropertyImages_PropertyNotFound_ShouldThrowModelNotFoundException() {
            when(propertyPersistencePort.existsPropertyById(propertyId)).thenReturn(false);

            ModelNotFoundException exception = assertThrows(ModelNotFoundException.class, () -> propertyImageUseCase.getPropertyImages(propertyId));
            assertEquals(String.format(DomainConstants.PROPERTY_IMAGE_PROPERTY_NOT_FOUND, propertyId), exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Tests para getPropertyImageById")
    class GetPropertyImageByIdTests {

        @Test
        @DisplayName("Debería retornar la imagen si existe")
        void getPropertyImageById_ImageExists_ShouldReturnImage() {
            when(propertyImagePersistencePort.findById(imageId)).thenReturn(Optional.of(validPropertyImage));
            PropertyImageModel result = propertyImageUseCase.getPropertyImageById(imageId);
            assertNotNull(result);
            assertEquals(imageId, result.getId());
        }

        @Test
        @DisplayName("Debería lanzar ModelNotFoundException si la imagen no existe")
        void getPropertyImageById_ImageNotFound_ShouldThrowModelNotFoundException() {
            when(propertyImagePersistencePort.findById(imageId)).thenReturn(Optional.empty());
            ModelNotFoundException exception = assertThrows(ModelNotFoundException.class, () -> propertyImageUseCase.getPropertyImageById(imageId));
            assertEquals(String.format(DomainConstants.PROPERTY_IMAGE_NOT_FOUND_BY_ID, imageId), exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Tests para deletePropertyImage")
    class DeletePropertyImageTests {

        @Test
        @DisplayName("Debería eliminar la imagen y el archivo")
        void deletePropertyImage_ImageExists_ShouldDeleteImageAndFile() throws IOException {
            when(propertyImagePersistencePort.findById(imageId)).thenReturn(Optional.of(validPropertyImage));
            doNothing().when(fileStorageService).deleteFile(anyString());
            doNothing().when(propertyImagePersistencePort).deleteById(imageId);

            propertyImageUseCase.deletePropertyImage(imageId);

            verify(fileStorageService).deleteFile(validPropertyImage.getImageUrl());
            verify(propertyImagePersistencePort).deleteById(imageId);
        }

        @Test
        @DisplayName("Debería lanzar ModelNotFoundException si la imagen a eliminar no existe")
        void deletePropertyImage_ImageNotFound_ShouldThrowModelNotFoundException() {
            when(propertyImagePersistencePort.findById(imageId)).thenReturn(Optional.empty());
            ModelNotFoundException exception = assertThrows(ModelNotFoundException.class, () -> propertyImageUseCase.deletePropertyImage(imageId));
            assertEquals(String.format(DomainConstants.PROPERTY_IMAGE_NOT_FOUND_FOR_DELETE, imageId), exception.getMessage());
        }

        @Test
        @DisplayName("Debería continuar con la eliminación en la BD si falla la eliminación del archivo físico")
        void deletePropertyImage_FileDeletionFails_ShouldStillDeleteFromDb() throws IOException {
            when(propertyImagePersistencePort.findById(imageId)).thenReturn(Optional.of(validPropertyImage));
            doThrow(new IOException("Failed to delete file")).when(fileStorageService).deleteFile(anyString());
            doNothing().when(propertyImagePersistencePort).deleteById(imageId);

            propertyImageUseCase.deletePropertyImage(imageId);

            verify(fileStorageService).deleteFile(validPropertyImage.getImageUrl());
            verify(propertyImagePersistencePort).deleteById(imageId);
        }
    }

    @Nested
    @DisplayName("Tests para setMainPropertyImage")
    class SetMainPropertyImageTests {

        @Test
        @DisplayName("Debería establecer la imagen como principal correctamente")
        void setMainPropertyImage_ValidData_ShouldSetMainImage() {
            validPropertyImage.setPropertyId(propertyId);
            when(propertyPersistencePort.existsPropertyById(propertyId)).thenReturn(true);
            when(propertyImagePersistencePort.findById(imageId)).thenReturn(Optional.of(validPropertyImage));
            doNothing().when(propertyImagePersistencePort).clearMainImageFlag(propertyId);
            when(propertyImagePersistencePort.save(any(PropertyImageModel.class))).thenReturn(validPropertyImage);

            propertyImageUseCase.setMainPropertyImage(propertyId, imageId);

            verify(propertyImagePersistencePort).clearMainImageFlag(propertyId);
            verify(propertyImagePersistencePort).save(propertyImageModelCaptor.capture());
            assertTrue(propertyImageModelCaptor.getValue().getIsMainImage());
            assertEquals(imageId, propertyImageModelCaptor.getValue().getId());
        }

        @Test
        @DisplayName("Debería lanzar ModelNotFoundException si la propiedad no existe")
        void setMainPropertyImage_PropertyNotFound_ShouldThrowModelNotFoundException() {
            when(propertyPersistencePort.existsPropertyById(propertyId)).thenReturn(false);
            ModelNotFoundException exception = assertThrows(ModelNotFoundException.class, () -> propertyImageUseCase.setMainPropertyImage(propertyId, imageId));
            assertEquals(String.format(DomainConstants.PROPERTY_IMAGE_PROPERTY_NOT_FOUND, propertyId), exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar ModelNotFoundException si la imagen no existe")
        void setMainPropertyImage_ImageNotFound_ShouldThrowModelNotFoundException() {
            when(propertyPersistencePort.existsPropertyById(propertyId)).thenReturn(true);
            when(propertyImagePersistencePort.findById(imageId)).thenReturn(Optional.empty());
            ModelNotFoundException exception = assertThrows(ModelNotFoundException.class, () -> propertyImageUseCase.setMainPropertyImage(propertyId, imageId));
            assertEquals(String.format(DomainConstants.PROPERTY_IMAGE_NOT_FOUND_BY_ID, imageId), exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar ValidationException si la imagen no pertenece a la propiedad")
        void setMainPropertyImage_ImageDoesNotBelongToProperty_ShouldThrowValidationException() {
            PropertyImageModel imageFromOtherProperty = PropertyImageMock.createPropertyImageModelWithPropertyId(999L);
            imageFromOtherProperty.setId(imageId);

            when(propertyPersistencePort.existsPropertyById(propertyId)).thenReturn(true);
            when(propertyImagePersistencePort.findById(imageId)).thenReturn(Optional.of(imageFromOtherProperty));

            ValidationException exception = assertThrows(ValidationException.class, () -> propertyImageUseCase.setMainPropertyImage(propertyId, imageId));
            assertEquals(String.format(DomainConstants.PROPERTY_IMAGE_DOES_NOT_BELONG_TO_PROPERTY, imageId, propertyId), exception.getMessage());
        }
    }
}
