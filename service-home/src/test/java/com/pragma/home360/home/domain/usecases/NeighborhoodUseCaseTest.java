package com.pragma.home360.home.domain.usecases;

import com.pragma.home360.home.domain.exceptions.ValidationException;
import com.pragma.home360.home.domain.model.FilterModel;
import com.pragma.home360.home.domain.model.NeighborhoodModel;
import com.pragma.home360.home.domain.model.CityModel;
import com.pragma.home360.home.domain.ports.out.CityPersistencePort;
import com.pragma.home360.home.domain.ports.out.NeighborhoodPersistencePort;
import com.pragma.home360.home.domain.utils.constants.DomainConstants;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;
import com.pragma.home360.home.mocks.FilterRequestMock;
import com.pragma.home360.home.mocks.NeighborhoodMock;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NeighborhoodUseCaseTest {

    @Mock
    private NeighborhoodPersistencePort neighborhoodPersistencePort;

    @Mock
    private CityPersistencePort cityPersistencePort;

    @InjectMocks
    private NeighborhoodUseCase neighborhoodUseCase;

    @Captor
    private ArgumentCaptor<NeighborhoodModel> neighborhoodModelCaptor;

    @Captor
    private ArgumentCaptor<FilterModel> filterCaptor;

    private NeighborhoodModel validNeighborhood;
    private CityModel mockCity;
    private FilterModel validFilter;

    @BeforeEach
    void setUp() {
        validNeighborhood = NeighborhoodMock.createDefaultNeighborhoodModel();
        mockCity = new CityModel(1L, "Test City", "Test Description", 1L);
        validFilter = FilterRequestMock.createDefaultFilterRequest();
    }

    @Nested
    @DisplayName("Tests para método getNeighborhoodsByCity")
    class GetNeighborhoodsByCityMethodTests {

        @Test
        @DisplayName("Debería retornar lista paginada de barrios por ciudad")
        void getNeighborhoodsByCity_ValidCityId_ShouldReturnPaginatedNeighborhoods() {
            // Arrange
            Long cityId = 1L;
            List<NeighborhoodModel> neighborhoodList = Arrays.asList(
                    NeighborhoodMock.createDefaultNeighborhoodModel(),
                    NeighborhoodMock.createNeighborhoodModelWithId(2L)
            );

            PagedResult<NeighborhoodModel> expectedResponse = new PagedResult<>(
                    neighborhoodList,
                    validFilter.page(),
                    validFilter.size(),
                    2L,
                    10
            );

            when(cityPersistencePort.getCityById(cityId)).thenReturn(Optional.of(mockCity));
            when(neighborhoodPersistencePort.getNeighborhoodsByCity(eq(cityId), any(FilterModel.class)))
                    .thenReturn(expectedResponse);

            // Act
            PagedResult<NeighborhoodModel> result = neighborhoodUseCase.getNeighborhoodsByCity(cityId, validFilter);

            // Assert
            assertNotNull(result);
            assertEquals(expectedResponse.content().size(), result.content().size());
            assertEquals(expectedResponse.page(), result.page());
            assertEquals(expectedResponse.size(), result.size());
            assertEquals(expectedResponse.totalElements(), result.totalElements());
            assertEquals(expectedResponse.totalPages(), result.totalPages());

            verify(cityPersistencePort).getCityById(cityId);
            verify(neighborhoodPersistencePort).getNeighborhoodsByCity(eq(cityId), filterCaptor.capture());
            assertEquals(validFilter.page(), filterCaptor.getValue().page());
            assertEquals(validFilter.size(), filterCaptor.getValue().size());
        }


        @Test
        @DisplayName("Debería lanzar ValidationException cuando page es negativo")
        void getNeighborhoodsByCity_NegativePage_ShouldThrowValidationException() {
            // Arrange
            Long cityId = 1L;
            FilterModel invalidFilter = FilterRequestMock.createFilterRequestWithNegativePage();
            when(cityPersistencePort.getCityById(cityId)).thenReturn(Optional.of(mockCity));

            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> neighborhoodUseCase.getNeighborhoodsByCity(cityId, invalidFilter)
            );

            assertEquals(DomainConstants.PAGINATION_PAGE_NEGATIVE, exception.getMessage());
            verify(cityPersistencePort).getCityById(cityId);
            verify(neighborhoodPersistencePort, never()).getNeighborhoodsByCity(anyLong(), any(FilterModel.class));
        }
    }

    @Nested
    @DisplayName("Tests para método existsNeighborhoodByName")
    class ExistsNeighborhoodByNameMethodTests {

        @Test
        @DisplayName("Debería retornar true cuando el barrio existe")
        void existsNeighborhoodByName_NeighborhoodExists_ShouldReturnTrue() {
            // Arrange
            String neighborhoodName = "Existing Neighborhood";
            when(neighborhoodPersistencePort.existsNeighborhoodByName(neighborhoodName)).thenReturn(true);

            // Act
            boolean result = neighborhoodUseCase.existsNeighborhoodByName(neighborhoodName);

            // Assert
            assertTrue(result);
            verify(neighborhoodPersistencePort).existsNeighborhoodByName(neighborhoodName);
        }

        @Test
        @DisplayName("Debería retornar false cuando el barrio no existe")
        void existsNeighborhoodByName_NeighborhoodDoesNotExist_ShouldReturnFalse() {
            // Arrange
            String neighborhoodName = "Non-existing Neighborhood";
            when(neighborhoodPersistencePort.existsNeighborhoodByName(neighborhoodName)).thenReturn(false);

            // Act
            boolean result = neighborhoodUseCase.existsNeighborhoodByName(neighborhoodName);

            // Assert
            assertFalse(result);
            verify(neighborhoodPersistencePort).existsNeighborhoodByName(neighborhoodName);
        }
    }

    @Nested
    @DisplayName("Tests para método saveNeighborhood")
    class SaveNeighborhoodMethodTests {

        @Test
        @DisplayName("Debería guardar y retornar el barrio cuando es válido")
        void saveNeighborhood_ValidNeighborhood_ShouldSaveAndReturnNeighborhood() {
            // Arrange
            when(neighborhoodPersistencePort.existsNeighborhoodByName(anyString())).thenReturn(false);
            when(cityPersistencePort.getCityById(any(Long.class))).thenReturn(Optional.of(mockCity));
            when(neighborhoodPersistencePort.saveNeighborhood(any(NeighborhoodModel.class))).thenReturn(validNeighborhood);

            // Act
            NeighborhoodModel result = neighborhoodUseCase.saveNeighborhood(validNeighborhood);

            // Assert
            assertNotNull(result);
            assertEquals(validNeighborhood.getId(), result.getId());
            assertEquals(validNeighborhood.getName(), result.getName());
            assertEquals(validNeighborhood.getDescription(), result.getDescription());
            assertEquals(validNeighborhood.getCityId(), result.getCityId());

            verify(neighborhoodPersistencePort).existsNeighborhoodByName(validNeighborhood.getName().trim());
            verify(cityPersistencePort).getCityById(validNeighborhood.getCityId());
            verify(neighborhoodPersistencePort).saveNeighborhood(validNeighborhood);
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando el nombre está vacío")
        void saveNeighborhood_EmptyName_ShouldThrowValidationException() {
            // Arrange
            NeighborhoodModel invalidNeighborhood = NeighborhoodMock.createNeighborhoodModelWithEmptyName();

            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> neighborhoodUseCase.saveNeighborhood(invalidNeighborhood)
            );

            assertEquals(DomainConstants.NEIGHBORHOOD_NAME_CANNOT_BE_EMPTY, exception.getMessage());
            verify(neighborhoodPersistencePort, never()).existsNeighborhoodByName(anyString());
            verify(cityPersistencePort, never()).getCityById(any(Long.class));
            verify(neighborhoodPersistencePort, never()).saveNeighborhood(any(NeighborhoodModel.class));
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando la descripción está vacía")
        void saveNeighborhood_EmptyDescription_ShouldThrowValidationException() {
            // Arrange
            NeighborhoodModel invalidNeighborhood = NeighborhoodMock.createNeighborhoodModelWithEmptyDescription();

            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> neighborhoodUseCase.saveNeighborhood(invalidNeighborhood)
            );

            assertEquals(DomainConstants.NEIGHBORHOOD_DESCRIPTION_CANNOT_BE_EMPTY, exception.getMessage());
            verify(neighborhoodPersistencePort, never()).existsNeighborhoodByName(anyString());
            verify(cityPersistencePort, never()).getCityById(any(Long.class));
            verify(neighborhoodPersistencePort, never()).saveNeighborhood(any(NeighborhoodModel.class));
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando el nombre es demasiado largo")
        void saveNeighborhood_NameTooLong_ShouldThrowValidationException() {
            // Arrange
            String longName = "a".repeat(DomainConstants.NEIGHBORHOOD_NAME_MAX_LENGTH + 1);
            NeighborhoodModel invalidNeighborhood = NeighborhoodMock.createNeighborhoodModelWithName(longName);

            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> neighborhoodUseCase.saveNeighborhood(invalidNeighborhood)
            );

            assertEquals(String.format(DomainConstants.NEIGHBORHOOD_NAME_MAX_LENGTH_EXCEEDED, DomainConstants.NEIGHBORHOOD_NAME_MAX_LENGTH), exception.getMessage());
            verify(neighborhoodPersistencePort, never()).existsNeighborhoodByName(anyString());
            verify(cityPersistencePort, never()).getCityById(any(Long.class));
            verify(neighborhoodPersistencePort, never()).saveNeighborhood(any(NeighborhoodModel.class));
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando la descripción es demasiado larga")
        void saveNeighborhood_DescriptionTooLong_ShouldThrowValidationException() {
            // Arrange
            String longDescription = "a".repeat(DomainConstants.NEIGHBORHOOD_DESCRIPTION_MAX_LENGTH + 1);
            NeighborhoodModel invalidNeighborhood = NeighborhoodMock.createNeighborhoodModelWithDescription(longDescription);

            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> neighborhoodUseCase.saveNeighborhood(invalidNeighborhood)
            );

            assertEquals(String.format(DomainConstants.NEIGHBORHOOD_DESCRIPTION_MAX_LENGTH_EXCEEDED, DomainConstants.NEIGHBORHOOD_DESCRIPTION_MAX_LENGTH), exception.getMessage());
            verify(neighborhoodPersistencePort, never()).existsNeighborhoodByName(anyString());
            verify(cityPersistencePort, never()).getCityById(any(Long.class));
            verify(neighborhoodPersistencePort, never()).saveNeighborhood(any(NeighborhoodModel.class));
        }


    }

    @Nested
    @DisplayName("Tests para método getNeighborhoodById")
    class GetNeighborhoodByIdMethodTests {

        @Test
        @DisplayName("Debería retornar el barrio cuando existe")
        void getNeighborhoodById_NeighborhoodExists_ShouldReturnNeighborhood() {
            // Arrange
            when(neighborhoodPersistencePort.getNeighborhoodById(any(Long.class))).thenReturn(Optional.of(validNeighborhood));

            // Act
            Optional<NeighborhoodModel> result = neighborhoodUseCase.getNeighborhoodById(validNeighborhood.getId());

            // Assert
            assertTrue(result.isPresent());
            assertEquals(validNeighborhood.getId(), result.get().getId());
            assertEquals(validNeighborhood.getName(), result.get().getName());
            assertEquals(validNeighborhood.getDescription(), result.get().getDescription());
            assertEquals(validNeighborhood.getCityId(), result.get().getCityId());

            verify(neighborhoodPersistencePort).getNeighborhoodById(validNeighborhood.getId());
        }

        @Test
        @DisplayName("Debería retornar Optional vacío cuando el barrio no existe")
        void getNeighborhoodById_NeighborhoodDoesNotExist_ShouldReturnEmptyOptional() {
            // Arrange
            Long neighborhoodId = 999L;
            when(neighborhoodPersistencePort.getNeighborhoodById(neighborhoodId)).thenReturn(Optional.empty());

            // Act
            Optional<NeighborhoodModel> result = neighborhoodUseCase.getNeighborhoodById(neighborhoodId);

            // Assert
            assertFalse(result.isPresent());
            verify(neighborhoodPersistencePort).getNeighborhoodById(neighborhoodId);
        }
    }

    @Nested
    @DisplayName("Tests para método getAllNeighborhoods")
    class GetAllNeighborhoodsMethodTests {

        @Test
        @DisplayName("Debería retornar lista paginada de barrios")
        void getAllNeighborhoods_ShouldReturnPaginatedNeighborhoods() {
            // Arrange
            List<NeighborhoodModel> neighborhoodList = Arrays.asList(
                    NeighborhoodMock.createDefaultNeighborhoodModel(),
                    NeighborhoodMock.createNeighborhoodModelWithId(2L)
            );

            PagedResult<NeighborhoodModel> expectedResponse = new PagedResult<>(
                    neighborhoodList,
                    validFilter.page(),
                    validFilter.size(),
                    2L,
                    10
            );

            when(neighborhoodPersistencePort.getAllNeighborhoods(any(FilterModel.class))).thenReturn(expectedResponse);

            // Act
            PagedResult<NeighborhoodModel> result = neighborhoodUseCase.getAllNeighborhoods(validFilter);

            // Assert
            assertNotNull(result);
            assertEquals(expectedResponse.content().size(), result.content().size());
            assertEquals(expectedResponse.page(), result.page());
            assertEquals(expectedResponse.size(), result.size());
            assertEquals(expectedResponse.totalElements(), result.totalElements());
            assertEquals(expectedResponse.totalPages(), result.totalPages());

            verify(neighborhoodPersistencePort).getAllNeighborhoods(filterCaptor.capture());
            assertEquals(validFilter.page(), filterCaptor.getValue().page());
            assertEquals(validFilter.size(), filterCaptor.getValue().size());
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando page es negativo")
        void getAllNeighborhoods_NegativePage_ShouldThrowValidationException() {
            // Arrange
            FilterModel invalidFilter = FilterRequestMock.createFilterRequestWithNegativePage();

            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> neighborhoodUseCase.getAllNeighborhoods(invalidFilter)
            );

            assertEquals(DomainConstants.PAGINATION_PAGE_NEGATIVE, exception.getMessage());
            verify(neighborhoodPersistencePort, never()).getAllNeighborhoods(any(FilterModel.class));
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando size es inválido")
        void getAllNeighborhoods_InvalidSize_ShouldThrowValidationException() {
            // Arrange
            FilterModel invalidFilter = FilterRequestMock.createFilterRequestWithInvalidSize(0);

            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> neighborhoodUseCase.getAllNeighborhoods(invalidFilter)
            );

            assertEquals(DomainConstants.PAGINATION_SIZE_BETWEEN, exception.getMessage());
            verify(neighborhoodPersistencePort, never()).getAllNeighborhoods(any(FilterModel.class));
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando offset excede el máximo")
        void getAllNeighborhoods_OffsetTooLarge_ShouldThrowValidationException() {
            // Arrange
            FilterModel invalidFilter = FilterRequestMock.createFilterRequestWithExceededOffset();

            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> neighborhoodUseCase.getAllNeighborhoods(invalidFilter)
            );

            assertEquals(DomainConstants.PAGINATION_MAX_OFFSET, exception.getMessage());
            verify(neighborhoodPersistencePort, never()).getAllNeighborhoods(any(FilterModel.class));
        }
    }
}