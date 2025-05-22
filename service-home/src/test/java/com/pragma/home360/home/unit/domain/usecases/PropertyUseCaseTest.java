package com.pragma.home360.home.unit.domain.usecases;

import com.pragma.home360.home.domain.exceptions.ModelNotFoundException;
import com.pragma.home360.home.domain.exceptions.ValidationException;
import com.pragma.home360.home.domain.model.CategoryModel;
import com.pragma.home360.home.domain.model.LocationModel;
import com.pragma.home360.home.domain.model.PropertyFilterModel;
import com.pragma.home360.home.domain.model.PropertyModel;
import com.pragma.home360.home.domain.ports.out.CategoryPersistencePort;
import com.pragma.home360.home.domain.ports.out.LocationPersistencePort;
import com.pragma.home360.home.domain.ports.out.PropertyPersistencePort;
import com.pragma.home360.home.domain.usecases.PropertyUseCase;
import com.pragma.home360.home.domain.utils.constants.DomainConstants;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;
import com.pragma.home360.home.infrastructure.entities.enums.PropertyPublicationStatus;
import com.pragma.home360.home.unit.mocks.PropertyFilterMock;
import com.pragma.home360.home.unit.mocks.PropertyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) // Opcional: para evitar fallos por otros stubs innecesarios durante la refactorización
class PropertyUseCaseTest {

    @Mock
    private PropertyPersistencePort propertyPersistencePort;

    @Mock
    private LocationPersistencePort locationPersistencePort;

    @Mock
    private CategoryPersistencePort categoryPersistencePort;

    @InjectMocks
    private PropertyUseCase propertyUseCase;

    @Captor
    private ArgumentCaptor<PropertyModel> propertyModelCaptor;
    @Captor
    private ArgumentCaptor<PropertyFilterModel> filterCaptor;
    @Captor
    private ArgumentCaptor<Long> longCaptor;
    @Captor
    private ArgumentCaptor<List<PropertyModel>> propertyListCaptor;


    private PropertyModel validProperty;
    private LocationModel mockLocation;
    private CategoryModel mockCategory;
    private PropertyFilterModel validFilter;


    @BeforeEach
    void setUp() {
        mockLocation = new LocationModel();
        mockLocation.setId(1L);
        mockLocation.setAddress("Mock Address");


        mockCategory = new CategoryModel();
        mockCategory.setId(1L);
        mockCategory.setName("Mock Category");


        validProperty = PropertyMock.createDefaultPropertyModel();
        validProperty.setLocation(mockLocation);
        validProperty.setCategory(mockCategory);
        validFilter = PropertyFilterMock.createDefaultPropertyFilter();
    }

    @Nested
    @DisplayName("Tests para método saveProperty")
    class SavePropertyMethodTests {

        @Test
        @DisplayName("Debería guardar y retornar la propiedad cuando es válida y fecha de publicación es hoy")
        void saveProperty_ValidPropertyTodayDate_ShouldSaveAndSetPublished() {
            validProperty.setActivePublicationDate(LocalDate.now());
            when(categoryPersistencePort.getCategoryById(anyLong())).thenReturn(Optional.of(mockCategory));
            when(locationPersistencePort.getLocationById(anyLong())).thenReturn(Optional.of(mockLocation));
            when(propertyPersistencePort.saveProperty(any(PropertyModel.class))).thenReturn(validProperty);

            PropertyModel result = propertyUseCase.saveProperty(validProperty);

            assertNotNull(result);
            assertEquals(PropertyPublicationStatus.PUBLISHED, result.getPublicationStatus());
            verify(propertyPersistencePort).saveProperty(propertyModelCaptor.capture());
            assertEquals(PropertyPublicationStatus.PUBLISHED, propertyModelCaptor.getValue().getPublicationStatus());
        }

        @Test
        @DisplayName("Debería guardar y retornar la propiedad cuando es válida y fecha de publicación es futura")
        void saveProperty_ValidPropertyFutureDate_ShouldSaveAndSetPending() {
            validProperty.setActivePublicationDate(LocalDate.now().plusDays(1));
            when(categoryPersistencePort.getCategoryById(anyLong())).thenReturn(Optional.of(mockCategory));
            when(locationPersistencePort.getLocationById(anyLong())).thenReturn(Optional.of(mockLocation));
            when(propertyPersistencePort.saveProperty(any(PropertyModel.class))).thenReturn(validProperty);

            PropertyModel result = propertyUseCase.saveProperty(validProperty);

            assertNotNull(result);
            assertEquals(PropertyPublicationStatus.PUBLICATION_PENDING, result.getPublicationStatus());
            verify(propertyPersistencePort).saveProperty(propertyModelCaptor.capture());
            assertEquals(PropertyPublicationStatus.PUBLICATION_PENDING, propertyModelCaptor.getValue().getPublicationStatus());
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando el nombre está vacío")
        void saveProperty_EmptyName_ShouldThrowValidationException() {
            PropertyModel invalidProperty = PropertyMock.createPropertyModelWithEmptyName();
            ValidationException exception = assertThrows(ValidationException.class, () -> propertyUseCase.saveProperty(invalidProperty));
            assertEquals(DomainConstants.PROPERTY_NAME_CANNOT_BE_EMPTY, exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando el nombre es nulo")
        void saveProperty_NullName_ShouldThrowValidationException() {
            PropertyModel invalidProperty = PropertyMock.createPropertyModelWithNullName();
            ValidationException exception = assertThrows(ValidationException.class, () -> propertyUseCase.saveProperty(invalidProperty));
            assertEquals(DomainConstants.PROPERTY_NAME_CANNOT_BE_EMPTY, exception.getMessage());
        }


        @Test
        @DisplayName("Debería lanzar ValidationException cuando el nombre excede la longitud máxima")
        void saveProperty_NameTooLong_ShouldThrowValidationException() {
            PropertyModel invalidProperty = PropertyMock.createPropertyModelWithName("a".repeat(DomainConstants.PROPERTY_NAME_MAX_LENGTH + 1));
            ValidationException exception = assertThrows(ValidationException.class, () -> propertyUseCase.saveProperty(invalidProperty));
            assertEquals(String.format(DomainConstants.PROPERTY_NAME_MAX_LENGTH_EXCEEDED, DomainConstants.PROPERTY_NAME_MAX_LENGTH), exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando el precio es negativo")
        void saveProperty_NegativePrice_ShouldThrowValidationException() {
            PropertyModel invalidProperty = PropertyMock.createPropertyModelWithNegativePrice();
            ValidationException exception = assertThrows(ValidationException.class, () -> propertyUseCase.saveProperty(invalidProperty));
            assertEquals(DomainConstants.PROPERTY_PRICE_MUST_BE_POSITIVE, exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando el precio es cero")
        void saveProperty_ZeroPrice_ShouldThrowValidationException() {
            PropertyModel invalidProperty = PropertyMock.createPropertyModelWithZeroPrice();
            ValidationException exception = assertThrows(ValidationException.class, () -> propertyUseCase.saveProperty(invalidProperty));
            assertEquals(DomainConstants.PROPERTY_PRICE_MUST_BE_POSITIVE, exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando el número de habitaciones es cero")
        void saveProperty_ZeroRooms_ShouldThrowValidationException() {
            PropertyModel invalidProperty = PropertyMock.createPropertyModelWithZeroRooms();
            ValidationException exception = assertThrows(ValidationException.class, () -> propertyUseCase.saveProperty(invalidProperty));
            assertEquals(DomainConstants.PROPERTY_ROOMS_MUST_BE_POSITIVE, exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando el número de baños es cero")
        void saveProperty_ZeroBathrooms_ShouldThrowValidationException() {
            PropertyModel invalidProperty = PropertyMock.createPropertyModelWithZeroBathrooms();
            ValidationException exception = assertThrows(ValidationException.class, () -> propertyUseCase.saveProperty(invalidProperty));
            assertEquals(DomainConstants.PROPERTY_BATHROOMS_MUST_BE_POSITIVE, exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando la fecha de publicación activa es en el pasado")
        void saveProperty_PastActiveDate_ShouldThrowValidationException() {
            PropertyModel invalidProperty = PropertyMock.createPropertyModelWithPastActiveDate();
            ValidationException exception = assertThrows(ValidationException.class, () -> propertyUseCase.saveProperty(invalidProperty));
            assertEquals(DomainConstants.PROPERTY_ACTIVE_DATE_IN_PAST, exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando la fecha de publicación activa excede el límite de un mes")
        void saveProperty_FutureActiveDateExceedsLimit_ShouldThrowValidationException() {
            PropertyModel invalidProperty = PropertyMock.createPropertyModelWithFutureActiveDateExceedsLimit();
            ValidationException exception = assertThrows(ValidationException.class, () -> propertyUseCase.saveProperty(invalidProperty));
            assertEquals(DomainConstants.PROPERTY_ACTIVE_DATE_EXCEEDS_LIMIT, exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando el ID de categoría es nulo en el objeto CategoryModel")
        void saveProperty_NullCategoryId_ShouldThrowValidationException() {
            PropertyModel invalidProperty = PropertyMock.createPropertyModelWithNullCategoryId();
            invalidProperty.setLocation(mockLocation);
            ValidationException exception = assertThrows(ValidationException.class, () -> propertyUseCase.saveProperty(invalidProperty));
            assertEquals(DomainConstants.PROPERTY_CATEGORY_ID_REQUIRED, exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando el objeto CategoryModel es nulo")
        void saveProperty_NullCategoryObject_ShouldThrowValidationException() {
            PropertyModel invalidProperty = PropertyMock.createPropertyModelWithNullCategoryObject();
            invalidProperty.setLocation(mockLocation);
            ValidationException exception = assertThrows(ValidationException.class, () -> propertyUseCase.saveProperty(invalidProperty));
            assertEquals(DomainConstants.PROPERTY_CATEGORY_ID_REQUIRED, exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar ModelNotFoundException cuando la categoría no existe")
        void saveProperty_CategoryNotFound_ShouldThrowModelNotFoundException() {
            validProperty.setCategory(new CategoryModel(999L, null, null));
            when(categoryPersistencePort.getCategoryById(999L)).thenReturn(Optional.empty());
            // La siguiente línea era el stub innecesario y ha sido eliminada:
            // when(locationPersistencePort.getLocationById(anyLong())).thenReturn(Optional.of(mockLocation));

            ModelNotFoundException exception = assertThrows(ModelNotFoundException.class, () -> propertyUseCase.saveProperty(validProperty));
            assertEquals(String.format(DomainConstants.CATEGORY_NOT_FOUND_BY_ID, 999L), exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando el ID de ubicación es nulo en el objeto LocationModel")
        void saveProperty_NullLocationId_ShouldThrowValidationException() {
            PropertyModel invalidProperty = PropertyMock.createPropertyModelWithNullLocationId();
            invalidProperty.setCategory(mockCategory);
            when(categoryPersistencePort.getCategoryById(anyLong())).thenReturn(Optional.of(mockCategory));
            ValidationException exception = assertThrows(ValidationException.class, () -> propertyUseCase.saveProperty(invalidProperty));
            assertEquals(DomainConstants.PROPERTY_LOCATION_ID_REQUIRED, exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando el objeto LocationModel es nulo")
        void saveProperty_NullLocationObject_ShouldThrowValidationException() {
            PropertyModel invalidProperty = PropertyMock.createPropertyModelWithNullLocationObject();
            invalidProperty.setCategory(mockCategory);
            when(categoryPersistencePort.getCategoryById(anyLong())).thenReturn(Optional.of(mockCategory));
            ValidationException exception = assertThrows(ValidationException.class, () -> propertyUseCase.saveProperty(invalidProperty));
            assertEquals(DomainConstants.PROPERTY_LOCATION_ID_REQUIRED, exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar ModelNotFoundException cuando la ubicación no existe")
        void saveProperty_LocationNotFound_ShouldThrowModelNotFoundException() {
            validProperty.setLocation(new LocationModel(999L, null, null, null, null, null, null, null));
            when(categoryPersistencePort.getCategoryById(anyLong())).thenReturn(Optional.of(mockCategory));
            when(locationPersistencePort.getLocationById(999L)).thenReturn(Optional.empty());

            ModelNotFoundException exception = assertThrows(ModelNotFoundException.class, () -> propertyUseCase.saveProperty(validProperty));
            assertEquals(String.format(DomainConstants.LOCATION_NOT_FOUND_BY_ID, 999L), exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Tests para método getPropertyById")
    class GetPropertyByIdMethodTests {
        @Test
        @DisplayName("Debería retornar la propiedad cuando existe")
        void getPropertyById_PropertyExists_ShouldReturnProperty() {
            when(propertyPersistencePort.getPropertyById(1L)).thenReturn(Optional.of(validProperty));
            PropertyModel result = propertyUseCase.getPropertyById(1L);
            assertNotNull(result);
            assertEquals(validProperty.getId(), result.getId());
            verify(propertyPersistencePort).getPropertyById(1L);
        }

        @Test
        @DisplayName("Debería retornar nulo cuando la propiedad no existe")
        void getPropertyById_PropertyDoesNotExist_ShouldReturnNull() {
            when(propertyPersistencePort.getPropertyById(999L)).thenReturn(Optional.empty());
            PropertyModel result = propertyUseCase.getPropertyById(999L);
            assertNull(result);
            verify(propertyPersistencePort).getPropertyById(999L);
        }
    }

    @Nested
    @DisplayName("Tests para método getAllProperties")
    class GetAllPropertiesMethodTests {
        @Test
        @DisplayName("Debería retornar propiedades paginadas con filtro válido")
        void getAllProperties_ValidFilter_ShouldReturnPagedProperties() {
            List<PropertyModel> propertyList = Collections.singletonList(validProperty);
            PagedResult<PropertyModel> expectedPagedResult = new PagedResult<>(propertyList, 0, 10, 1L, 1);
            when(propertyPersistencePort.getAllProperties(any(PropertyFilterModel.class))).thenReturn(expectedPagedResult);

            PagedResult<PropertyModel> result = propertyUseCase.getAllProperties(validFilter);

            assertNotNull(result);
            assertEquals(1, result.content().size());
            verify(propertyPersistencePort).getAllProperties(filterCaptor.capture());
            assertEquals(validFilter.page(), filterCaptor.getValue().page());
        }

        @Test
        @DisplayName("Debería normalizar el término de búsqueda antes de pasarlo al puerto")
        void getAllProperties_WithSearchTerm_ShouldNormalizeSearchTerm() {
            PropertyFilterModel filterWithSearch = PropertyFilterMock.createPropertyFilterWithSearchTerm("Casa con Jardín");
            String expectedNormalizedSearchTerm = "casa con jardin";

            PagedResult<PropertyModel> emptyResult = new PagedResult<>(Collections.emptyList(), 0, 10, 0L, 0);
            when(propertyPersistencePort.getAllProperties(any(PropertyFilterModel.class))).thenReturn(emptyResult);

            propertyUseCase.getAllProperties(filterWithSearch);

            verify(propertyPersistencePort).getAllProperties(filterCaptor.capture());
            assertEquals(expectedNormalizedSearchTerm, filterCaptor.getValue().searchTerm());
        }

        @Test
        @DisplayName("Debería lanzar ValidationException con página negativa")
        void getAllProperties_NegativePage_ShouldThrowValidationException() {
            PropertyFilterModel invalidFilter = PropertyFilterMock.createPropertyFilterWithNegativePage();
            ValidationException exception = assertThrows(ValidationException.class, () -> propertyUseCase.getAllProperties(invalidFilter));
            assertEquals(DomainConstants.PAGINATION_PAGE_NEGATIVE, exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 51})
        @DisplayName("Debería lanzar ValidationException con tamaño inválido")
        void getAllProperties_InvalidSize_ShouldThrowValidationException(int size) {
            PropertyFilterModel invalidFilter = PropertyFilterMock.createPropertyFilterWithSize(size);
            ValidationException exception = assertThrows(ValidationException.class, () -> propertyUseCase.getAllProperties(invalidFilter));
            assertEquals(DomainConstants.PAGINATION_SIZE_BETWEEN, exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando el offset excede el límite")
        void getAllProperties_ExceededOffset_ShouldThrowValidationException() {
            PropertyFilterModel invalidFilter = PropertyFilterMock.createPropertyFilterWithExceededOffset();
            ValidationException exception = assertThrows(ValidationException.class, () -> propertyUseCase.getAllProperties(invalidFilter));
            assertEquals(DomainConstants.PAGINATION_MAX_OFFSET, exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Tests para método updateProperty")
    class UpdatePropertyMethodTests {

        private PropertyModel existingProperty;
        private PropertyModel updateData;

        @BeforeEach
        void setUp() {
            existingProperty = PropertyMock.createPropertyModelWithId(1L);
            existingProperty.setCreatedAt(validProperty.getCreatedAt()); // Simular una fecha de creación
            existingProperty.setPublicationStatus(PropertyPublicationStatus.PUBLISHED);
            existingProperty.setActivePublicationDate(LocalDate.now());
            existingProperty.setCategory(mockCategory); // Asegurar que la propiedad existente tenga categoría y ubicación
            existingProperty.setLocation(mockLocation);


            updateData = PropertyMock.createDefaultPropertyModel();
            updateData.setId(1L);
            updateData.setName("Updated Name");
            updateData.setActivePublicationDate(LocalDate.now().plusDays(1)); // Fecha futura para que sea PENDING
            updateData.setCategory(new CategoryModel(2L, null, null));
            updateData.setLocation(new LocationModel(2L, null, null, null, null, null, null, null));
        }

        @Test
        @DisplayName("Debería actualizar la propiedad correctamente con datos válidos")
        void updateProperty_ValidData_ShouldUpdateAndSetStatus() {
            when(propertyPersistencePort.getPropertyById(1L)).thenReturn(Optional.of(existingProperty));
            when(categoryPersistencePort.getCategoryById(2L)).thenReturn(Optional.of(new CategoryModel(2L, "New Category", "Desc")));
            when(locationPersistencePort.getLocationById(2L)).thenReturn(Optional.of(new LocationModel(2L, "New Address", 0.0,0.0,null,null,null,null)));
            doNothing().when(propertyPersistencePort).updateProperty(anyLong(), any(PropertyModel.class));

            propertyUseCase.updateProperty(1L, updateData);

            verify(propertyPersistencePort).updateProperty(longCaptor.capture(), propertyModelCaptor.capture());
            assertEquals(1L, longCaptor.getValue());
            PropertyModel capturedProperty = propertyModelCaptor.getValue();
            assertEquals("Updated Name", capturedProperty.getName());
            assertEquals(PropertyPublicationStatus.PUBLICATION_PENDING, capturedProperty.getPublicationStatus());
            assertEquals(existingProperty.getCreatedAt(), capturedProperty.getCreatedAt());
        }

        @Test
        @DisplayName("Debería lanzar ModelNotFoundException si la propiedad a actualizar no existe")
        void updateProperty_PropertyNotFound_ShouldThrowModelNotFoundException() {
            when(propertyPersistencePort.getPropertyById(999L)).thenReturn(Optional.empty());
            ModelNotFoundException exception = assertThrows(ModelNotFoundException.class, () -> propertyUseCase.updateProperty(999L, updateData));
            assertEquals(String.format(DomainConstants.PROPERTY_NOT_FOUND_FOR_UPDATE, 999L), exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar ValidationException si el nombre está vacío")
        void updateProperty_EmptyName_ShouldThrowValidationException() {
            when(propertyPersistencePort.getPropertyById(1L)).thenReturn(Optional.of(existingProperty));
            updateData.setName("");
            ValidationException exception = assertThrows(ValidationException.class, () -> propertyUseCase.updateProperty(1L, updateData));
            assertEquals(DomainConstants.PROPERTY_NAME_CANNOT_BE_EMPTY, exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar ValidationException si la fecha de publicación activa es nula")
        void updateProperty_NullActiveDate_ShouldThrowValidationException() {
            when(propertyPersistencePort.getPropertyById(1L)).thenReturn(Optional.of(existingProperty));
            updateData.setActivePublicationDate(null);
            ValidationException exception = assertThrows(ValidationException.class, () -> propertyUseCase.updateProperty(1L, updateData));
            assertEquals(DomainConstants.PROPERTY_ACTIVE_DATE_REQUIRED, exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar ValidationException si se intenta cambiar fecha activa a pasado para propiedad publicada")
        void updateProperty_ChangeActiveDateToPastForPublished_ShouldThrowValidationException() {
            existingProperty.setPublicationStatus(PropertyPublicationStatus.PUBLISHED);
            existingProperty.setActivePublicationDate(LocalDate.now());
            when(propertyPersistencePort.getPropertyById(1L)).thenReturn(Optional.of(existingProperty));
            updateData.setActivePublicationDate(LocalDate.now().minusDays(1));

            ValidationException exception = assertThrows(ValidationException.class, () -> propertyUseCase.updateProperty(1L, updateData));
            assertEquals(DomainConstants.PROPERTY_CANNOT_CHANGE_ACTIVE_DATE_TO_PAST, exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar ValidationException si la fecha activa es en el pasado (no publicada)")
        void updateProperty_ActiveDateInPast_ShouldThrowValidationException() {
            existingProperty.setPublicationStatus(PropertyPublicationStatus.PUBLICATION_PENDING);
            when(propertyPersistencePort.getPropertyById(1L)).thenReturn(Optional.of(existingProperty));
            updateData.setActivePublicationDate(LocalDate.now().minusDays(1));

            ValidationException exception = assertThrows(ValidationException.class, () -> propertyUseCase.updateProperty(1L, updateData));
            assertEquals(DomainConstants.PROPERTY_ACTIVE_DATE_IN_PAST, exception.getMessage());
        }


        @Test
        @DisplayName("Debería lanzar ValidationException si la fecha activa excede el límite")
        void updateProperty_ActiveDateExceedsLimit_ShouldThrowValidationException() {
            when(propertyPersistencePort.getPropertyById(1L)).thenReturn(Optional.of(existingProperty));
            updateData.setActivePublicationDate(LocalDate.now().plusMonths(2));
            ValidationException exception = assertThrows(ValidationException.class, () -> propertyUseCase.updateProperty(1L, updateData));
            assertEquals(DomainConstants.PROPERTY_ACTIVE_DATE_EXCEEDS_LIMIT, exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar ModelNotFoundException si la nueva categoría no existe")
        void updateProperty_NewCategoryNotFound_ShouldThrowModelNotFoundException() {
            when(propertyPersistencePort.getPropertyById(1L)).thenReturn(Optional.of(existingProperty));
            when(categoryPersistencePort.getCategoryById(2L)).thenReturn(Optional.empty());
            updateData.setCategory(new CategoryModel(2L, null, null));

            ModelNotFoundException exception = assertThrows(ModelNotFoundException.class, () -> propertyUseCase.updateProperty(1L, updateData));
            assertEquals(String.format(DomainConstants.CATEGORY_NOT_FOUND_BY_ID, 2L), exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar ModelNotFoundException si la nueva ubicación no existe")
        void updateProperty_NewLocationNotFound_ShouldThrowModelNotFoundException() {
            when(propertyPersistencePort.getPropertyById(1L)).thenReturn(Optional.of(existingProperty));
            when(categoryPersistencePort.getCategoryById(anyLong())).thenReturn(Optional.of(new CategoryModel(1L, "Cat", "Desc")));
            when(locationPersistencePort.getLocationById(2L)).thenReturn(Optional.empty());
            updateData.setLocation(new LocationModel(2L, null, null, null, null, null, null, null));

            ModelNotFoundException exception = assertThrows(ModelNotFoundException.class, () -> propertyUseCase.updateProperty(1L, updateData));
            assertEquals(String.format(DomainConstants.LOCATION_NOT_FOUND_BY_ID, 2L), exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Tests para método deleteProperty")
    class DeletePropertyMethodTests {
        @Test
        @DisplayName("Debería eliminar la propiedad si existe")
        void deleteProperty_PropertyExists_ShouldDelete() {
            when(propertyPersistencePort.existsPropertyById(1L)).thenReturn(true);
            doNothing().when(propertyPersistencePort).deleteProperty(1L);
            propertyUseCase.deleteProperty(1L);
            verify(propertyPersistencePort).deleteProperty(1L);
        }

        @Test
        @DisplayName("Debería lanzar ModelNotFoundException si la propiedad no existe")
        void deleteProperty_PropertyNotFound_ShouldThrowModelNotFoundException() {
            when(propertyPersistencePort.existsPropertyById(999L)).thenReturn(false);
            ModelNotFoundException exception = assertThrows(ModelNotFoundException.class, () -> propertyUseCase.deleteProperty(999L));
            assertEquals(String.format(DomainConstants.PROPERTY_NOT_FOUND_FOR_DELETE, 999L), exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Tests para método existsPropertyById")
    class ExistsPropertyByIdMethodTests {
        @Test
        @DisplayName("Debería retornar true si la propiedad existe")
        void existsPropertyById_PropertyExists_ShouldReturnTrue() {
            when(propertyPersistencePort.existsPropertyById(1L)).thenReturn(true);
            assertTrue(propertyUseCase.existsPropertyById(1L));
        }

        @Test
        @DisplayName("Debería retornar false si la propiedad no existe")
        void existsPropertyById_PropertyDoesNotExist_ShouldReturnFalse() {
            when(propertyPersistencePort.existsPropertyById(999L)).thenReturn(false);
            assertFalse(propertyUseCase.existsPropertyById(999L));
        }
    }

    @Nested
    @DisplayName("Tests para método processPendingPropertiesToPublish")
    class ProcessPendingPropertiesToPublishMethodTests {

        @Test
        @DisplayName("Debería actualizar propiedades pendientes a publicadas")
        void processPendingPropertiesToPublish_WithPendingProperties_ShouldUpdateStatus() {
            PropertyModel pendingProp1 = PropertyMock.createPropertyModelWithStatus(PropertyPublicationStatus.PUBLICATION_PENDING);
            pendingProp1.setId(10L);
            PropertyModel pendingProp2 = PropertyMock.createPropertyModelWithStatus(PropertyPublicationStatus.PUBLICATION_PENDING);
            pendingProp2.setId(11L);
            List<PropertyModel> pendingProperties = List.of(pendingProp1, pendingProp2);

            when(propertyPersistencePort.findByPublicationStatusAndActiveDateLessThanEqual(
                    eq(PropertyPublicationStatus.PUBLICATION_PENDING),
                    any(LocalDate.class))
            ).thenReturn(pendingProperties);

            doNothing().when(propertyPersistencePort).updateProperties(anyList());

            propertyUseCase.processPendingPropertiesToPublish();

            verify(propertyPersistencePort).updateProperties(propertyListCaptor.capture());
            List<PropertyModel> capturedList = propertyListCaptor.getValue();
            assertEquals(2, capturedList.size());
            assertTrue(capturedList.stream().allMatch(p -> p.getPublicationStatus() == PropertyPublicationStatus.PUBLISHED));
            assertEquals(10L, capturedList.get(0).getId());
            assertEquals(11L, capturedList.get(1).getId());
        }

        @Test
        @DisplayName("No debería hacer nada si no hay propiedades pendientes")
        void processPendingPropertiesToPublish_NoPendingProperties_ShouldDoNothing() {
            when(propertyPersistencePort.findByPublicationStatusAndActiveDateLessThanEqual(
                    eq(PropertyPublicationStatus.PUBLICATION_PENDING),
                    any(LocalDate.class))
            ).thenReturn(Collections.emptyList());

            propertyUseCase.processPendingPropertiesToPublish();

            verify(propertyPersistencePort, never()).updateProperties(anyList());
        }
    }
}
