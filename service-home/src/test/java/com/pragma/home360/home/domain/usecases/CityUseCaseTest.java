package com.pragma.home360.home.domain.usecases;

import com.pragma.home360.home.domain.exceptions.ValidationException;
import com.pragma.home360.home.domain.model.CityModel;
import com.pragma.home360.home.domain.model.DepartmentModel;
import com.pragma.home360.home.domain.ports.out.CityPersistencePort;
import com.pragma.home360.home.domain.ports.out.DepartmentPersistencePort;
import com.pragma.home360.home.domain.utils.constants.DomainConstants;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityUseCaseTest {

    @Mock
    private CityPersistencePort cityPersistencePort;

    @Mock
    private DepartmentPersistencePort departmentPersistencePort;

    @InjectMocks
    private CityUseCase cityUseCase;

    @Captor
    private ArgumentCaptor<CityModel> cityModelCaptor;

    private CityModel validCity;
    private DepartmentModel validDepartment;

    @BeforeEach
    void setUp() {
        validDepartment = new DepartmentModel(1L, "Test Department", "Test Description");
        validCity = new CityModel(1L, "Test City", "Test Description", validDepartment.getId());
    }

    @Nested
    @DisplayName("Tests para método saveCity")
    class SaveCityMethodTests {

        @Test
        @DisplayName("Debería guardar y retornar la ciudad cuando es válida")
        void saveCity_ValidCity_ShouldSaveAndReturnCity() {
            // Arrange
            when(cityPersistencePort.existsCityByName(anyString())).thenReturn(false);
            when(departmentPersistencePort.getDepartmentById(any(Long.class))).thenReturn(Optional.of(validDepartment));
            when(cityPersistencePort.saveCity(any(CityModel.class))).thenReturn(validCity);

            // Act
            CityModel result = cityUseCase.saveCity(validCity);

            // Assert
            assertNotNull(result);
            assertEquals(validCity.getId(), result.getId());
            assertEquals(validCity.getName(), result.getName());
            assertEquals(validCity.getDescription(), result.getDescription());
            assertEquals(validCity.getDepartmentId(), result.getDepartmentId());

            verify(cityPersistencePort).existsCityByName(validCity.getName().trim());
            verify(departmentPersistencePort).getDepartmentById(validCity.getDepartmentId());
            verify(cityPersistencePort).saveCity(validCity);
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando el nombre está vacío")
        void saveCity_EmptyName_ShouldThrowValidationException() {
            // Arrange
            CityModel invalidCity = new CityModel(1L, "", "Test Description", validDepartment.getId());

            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> cityUseCase.saveCity(invalidCity)
            );

            assertEquals(DomainConstants.CITY_NAME_CANNOT_BE_EMPTY, exception.getMessage());
            verify(cityPersistencePort, never()).existsCityByName(anyString());
            verify(departmentPersistencePort, never()).getDepartmentById(any(Long.class));
            verify(cityPersistencePort, never()).saveCity(any(CityModel.class));
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando el nombre es demasiado largo")
        void saveCity_NameTooLong_ShouldThrowValidationException() {
            // Arrange
            String longName = "a".repeat(DomainConstants.CITY_NAME_MAX_LENGTH + 1);
            CityModel invalidCity = new CityModel(1L, longName, "Test Description", validDepartment.getId());

            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> cityUseCase.saveCity(invalidCity)
            );

            assertEquals(DomainConstants.CITY_NAME_MAX_LENGTH_EXCEEDED, exception.getMessage());
            verify(cityPersistencePort, never()).existsCityByName(anyString());
            verify(departmentPersistencePort, never()).getDepartmentById(any(Long.class));
            verify(cityPersistencePort, never()).saveCity(any(CityModel.class));
        }

    }

    @Nested
    @DisplayName("Tests para método getCityById")
    class GetCityByIdMethodTests {

        @Test
        @DisplayName("Debería retornar la ciudad cuando existe")
        void getCityById_CityExists_ShouldReturnCity() {
            // Arrange
            when(cityPersistencePort.getCityById(any(Long.class))).thenReturn(Optional.of(validCity));

            // Act
            Optional<CityModel> result = cityUseCase.getCityById(validCity.getId());

            // Assert
            assertTrue(result.isPresent());
            assertEquals(validCity.getId(), result.get().getId());
            assertEquals(validCity.getName(), result.get().getName());
            assertEquals(validCity.getDescription(), result.get().getDescription());
            assertEquals(validCity.getDepartmentId(), result.get().getDepartmentId());

            verify(cityPersistencePort).getCityById(validCity.getId());
        }

        @Test
        @DisplayName("Debería retornar Optional vacío cuando la ciudad no existe")
        void getCityById_CityDoesNotExist_ShouldReturnEmptyOptional() {
            // Arrange
            Long cityId = 999L;
            when(cityPersistencePort.getCityById(cityId)).thenReturn(Optional.empty());

            // Act
            Optional<CityModel> result = cityUseCase.getCityById(cityId);

            // Assert
            assertFalse(result.isPresent());
            verify(cityPersistencePort).getCityById(cityId);
        }
    }

    @Nested
    @DisplayName("Tests para método getAllCities")
    class GetAllCitiesMethodTests {

        @Test
        @DisplayName("Debería retornar lista de ciudades")
        void getAllCities_ShouldReturnCityList() {
            // Arrange
            int page = 0;
            int size = 10;
            CityModel city1 = new CityModel(1L, "City 1", "Description 1", 1L);
            CityModel city2 = new CityModel(2L, "City 2", "Description 2", 1L);
            List<CityModel> expectedCities = Arrays.asList(city1, city2);

            when(cityPersistencePort.getAllCities(page, size)).thenReturn(expectedCities);

            // Act
            List<CityModel> result = cityUseCase.getAllCities(page, size);

            // Assert
            assertEquals(expectedCities.size(), result.size());
            assertEquals(expectedCities, result);
            verify(cityPersistencePort).getAllCities(page, size);
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando page es negativo")
        void getAllCities_NegativePage_ShouldThrowValidationException() {
            // Arrange
            int page = -1;
            int size = 10;

            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> cityUseCase.getAllCities(page, size)
            );

            assertEquals(DomainConstants.PAGINATION_PAGE_NEGATIVE, exception.getMessage());
            verify(cityPersistencePort, never()).getAllCities(anyInt(), anyInt());
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando size es menor que 1")
        void getAllCities_SizeTooSmall_ShouldThrowValidationException() {
            // Arrange
            int page = 0;
            int size = 0;

            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> cityUseCase.getAllCities(page, size)
            );

            assertEquals(DomainConstants.PAGINATION_SIZE_BETWEEN, exception.getMessage());
            verify(cityPersistencePort, never()).getAllCities(anyInt(), anyInt());
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando size es mayor que MAX_PAGE_SIZE")
        void getAllCities_SizeTooLarge_ShouldThrowValidationException() {
            // Arrange
            int page = 0;
            int size = DomainConstants.MAX_PAGE_SIZE + 1;

            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> cityUseCase.getAllCities(page, size)
            );

            assertEquals(DomainConstants.PAGINATION_SIZE_BETWEEN, exception.getMessage());
            verify(cityPersistencePort, never()).getAllCities(anyInt(), anyInt());
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando offset excede el máximo")
        void getAllCities_OffsetTooLarge_ShouldThrowValidationException() {
            // Arrange
            int page = 201;  // Con size=50, esto genera un offset mayor a 10,000
            int size = 50;

            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> cityUseCase.getAllCities(page, size)
            );

            assertEquals(DomainConstants.PAGINATION_MAX_OFFSET, exception.getMessage());
            verify(cityPersistencePort, never()).getAllCities(anyInt(), anyInt());
        }
    }

    @Nested
    @DisplayName("Tests para método getCityCount")
    class GetCityCountMethodTests {

        @Test
        @DisplayName("Debería retornar el conteo de ciudades")
        void getCityCount_ShouldReturnCount() {
            // Arrange
            long expectedCount = 10L;
            when(cityPersistencePort.getCityCount()).thenReturn(expectedCount);

            // Act
            long result = cityUseCase.getCityCount();

            // Assert
            assertEquals(expectedCount, result);
            verify(cityPersistencePort).getCityCount();
        }
    }
}