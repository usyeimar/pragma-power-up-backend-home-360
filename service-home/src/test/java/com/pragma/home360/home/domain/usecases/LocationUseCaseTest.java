package com.pragma.home360.home.domain.usecases;

import com.pragma.home360.home.domain.exceptions.ValidationException;
import com.pragma.home360.home.domain.model.LocationModel;
import com.pragma.home360.home.domain.ports.out.CityPersistencePort;
import com.pragma.home360.home.domain.ports.out.LocationPersistencePort;
import com.pragma.home360.home.domain.ports.out.NeighborhoodPersistencePort;
import com.pragma.home360.home.domain.utils.constants.DomainConstants;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;
import com.pragma.home360.home.mocks.LocationMock;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationUseCaseTest {

    @Mock
    private LocationPersistencePort locationPersistencePort;

    @Mock
    private NeighborhoodPersistencePort neighborhoodPersistencePort;

    @Mock
    private CityPersistencePort cityPersistencePort;

    @InjectMocks
    private LocationUseCase locationUseCase;

    @Captor
    private ArgumentCaptor<LocationModel> locationModelCaptor;

    private LocationModel validLocation;

    @BeforeEach
    void setUp() {
        validLocation = LocationMock.createDefaultLocationModel();
    }


    @Nested
    @DisplayName("Tests para método searchLocations")
    class SearchLocationsMethodTests {

        @Test
        @DisplayName("Debería buscar ubicaciones con parámetros válidos")
        void searchLocations_ValidParameters_ShouldReturnPagedResult() {
            // Arrange
            String searchText = "Medellín";
            int page = 0;
            int size = 10;
            String sortBy = "city";
            String sortDirection = "ASC";

            List<LocationModel> locationList = Arrays.asList(
                    LocationMock.createDefaultLocationModel(),
                    LocationMock.createLocationModelWithId(2L)
            );

            PagedResult<LocationModel> expectedResponse = new PagedResult<>(
                    locationList,
                    page,
                    size,
                    2L,
                    1
            );

            when(locationPersistencePort.searchLocations(anyString(), anyInt(), anyInt(), anyString(), anyString()))
                    .thenReturn(expectedResponse);

            // Act
            PagedResult<LocationModel> result = locationUseCase.searchLocations(
                    searchText, page, size, sortBy, sortDirection);

            // Assert
            assertNotNull(result);
            assertEquals(expectedResponse.content().size(), result.content().size());
            assertEquals(expectedResponse.page(), result.page());
            assertEquals(expectedResponse.size(), result.size());
            assertEquals(expectedResponse.totalElements(), result.totalElements());
            assertEquals(expectedResponse.totalPages(), result.totalPages());

            verify(locationPersistencePort).searchLocations(anyString(), eq(page), eq(size), eq(sortBy), eq(sortDirection));
        }

        @Test
        @DisplayName("Debería normalizar el texto de búsqueda")
        void searchLocations_ShouldNormalizeSearchText() {
            // Arrange
            String searchText = "Médellin Áéíóú";
            int page = 0;
            int size = 10;
            String sortBy = "city";
            String sortDirection = "ASC";

            // Crear una respuesta vacía
            PagedResult<LocationModel> expectedResponse = new PagedResult<>(
                    List.of(),
                    page,
                    size,
                    0,
                    10
            );

            when(locationPersistencePort.searchLocations(anyString(), anyInt(), anyInt(), anyString(), anyString()))
                    .thenReturn(expectedResponse);

            // Act
            locationUseCase.searchLocations(searchText, page, size, sortBy, sortDirection);

            // Assert
            verify(locationPersistencePort).searchLocations(
                    argThat(normalizedText -> !normalizedText.contains("é") && !normalizedText.contains("í") && !normalizedText.contains("ó") && !normalizedText.contains("ú")),
                    eq(page),
                    eq(size),
                    eq(sortBy),
                    eq(sortDirection)
            );
        }

        @Test
        @DisplayName("Debería manejar texto de búsqueda nulo correctamente")
        void searchLocations_NullSearchText_ShouldHandleCorrectly() {
            // Arrange
            String searchText = null;
            int page = 0;
            int size = 10;
            String sortBy = "city";
            String sortDirection = "ASC";

            // Crear una respuesta vacía
            PagedResult<LocationModel> expectedResponse = new PagedResult<>(
                    List.of(),
                    page,
                    size,
                    0,
                    10
            );

            when(locationPersistencePort.searchLocations(anyString(), anyInt(), anyInt(), anyString(), anyString()))
                    .thenReturn(expectedResponse);

            // Act & Assert
            assertDoesNotThrow(() -> locationUseCase.searchLocations(
                    searchText, page, size, sortBy, sortDirection));

            verify(locationPersistencePort).searchLocations(eq(""), eq(page), eq(size), eq(sortBy), eq(sortDirection));
        }


        @Test
        @DisplayName("Debería lanzar ValidationException cuando size es menor que 1")
        void searchLocations_SizeTooSmall_ShouldThrowValidationException() {
            // Arrange
            int page = 0;
            int size = 0;

            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> locationUseCase.searchLocations("text", page, size, "city", "ASC")
            );

            assertEquals(DomainConstants.PAGINATION_SIZE_BETWEEN, exception.getMessage());
            verify(locationPersistencePort, never()).searchLocations(anyString(), anyInt(), anyInt(), anyString(), anyString());
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando size es mayor que MAX_PAGE_SIZE")
        void searchLocations_SizeTooLarge_ShouldThrowValidationException() {
            // Arrange
            int page = 0;
            int size = DomainConstants.MAX_PAGE_SIZE + 1;

            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> locationUseCase.searchLocations("text", page, size, "city", "ASC")
            );

            assertEquals(DomainConstants.PAGINATION_SIZE_BETWEEN, exception.getMessage());
            verify(locationPersistencePort, never()).searchLocations(anyString(), anyInt(), anyInt(), anyString(), anyString());
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando offset excede el máximo")
        void searchLocations_OffsetTooLarge_ShouldThrowValidationException() {
            // Arrange
            int page = 201;  // Con size=50, esto genera un offset mayor a 10,000
            int size = 50;

            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> locationUseCase.searchLocations("text", page, size, "city", "ASC")
            );

            assertEquals(DomainConstants.PAGINATION_MAX_OFFSET, exception.getMessage());
            verify(locationPersistencePort, never()).searchLocations(anyString(), anyInt(), anyInt(), anyString(), anyString());
        }

        @ParameterizedTest
        @ValueSource(strings = {"invalid", "location", "address"})
        @DisplayName("Debería lanzar ValidationException cuando sortBy es inválido")
        void searchLocations_InvalidSortBy_ShouldThrowValidationException(String invalidSortBy) {
            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> locationUseCase.searchLocations("text", 0, 10, invalidSortBy, "ASC")
            );

            assertEquals(DomainConstants.LOCATION_SORT_FIELD_INVALID, exception.getMessage());
            verify(locationPersistencePort, never()).searchLocations(anyString(), anyInt(), anyInt(), anyString(), anyString());
        }

        @ParameterizedTest
        @ValueSource(strings = {"INVALID", "UP", "DOWN"})
        @DisplayName("Debería lanzar ValidationException cuando sortDirection es inválido")
        void searchLocations_InvalidSortDirection_ShouldThrowValidationException(String invalidDirection) {
            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> locationUseCase.searchLocations("text", 0, 10, "city", invalidDirection)
            );

            assertEquals(DomainConstants.LOCATION_SORT_DIRECTION_INVALID, exception.getMessage());
            verify(locationPersistencePort, never()).searchLocations(anyString(), anyInt(), anyInt(), anyString(), anyString());
        }
    }

    @Nested
    @DisplayName("Tests para método getLocationById")
    class GetLocationByIdMethodTests {

        @Test
        @DisplayName("Debería retornar la ubicación cuando existe")
        void getLocationById_LocationExists_ShouldReturnLocation() {
            // Arrange
            when(locationPersistencePort.getLocationById(anyLong())).thenReturn(Optional.of(validLocation));

            // Act
            Optional<LocationModel> result = locationUseCase.getLocationById(validLocation.getId());

            // Assert
            assertTrue(result.isPresent());
            assertEquals(validLocation.getId(), result.get().getId());
            verify(locationPersistencePort).getLocationById(validLocation.getId());
        }

        @Test
        @DisplayName("Debería retornar Optional vacío cuando la ubicación no existe")
        void getLocationById_LocationDoesNotExist_ShouldReturnEmptyOptional() {
            // Arrange
            Long locationId = 999L;
            when(locationPersistencePort.getLocationById(locationId)).thenReturn(Optional.empty());

            // Act
            Optional<LocationModel> result = locationUseCase.getLocationById(locationId);

            // Assert
            assertFalse(result.isPresent());
            verify(locationPersistencePort).getLocationById(locationId);
        }
    }
}