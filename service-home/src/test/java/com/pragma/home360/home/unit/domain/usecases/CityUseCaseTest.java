package com.pragma.home360.home.unit.domain.usecases;

import com.pragma.home360.home.domain.exceptions.ModelNotFoundException;
import com.pragma.home360.home.domain.exceptions.ValidationException;
import com.pragma.home360.home.domain.model.CityModel;
import com.pragma.home360.home.domain.model.DepartmentModel;
import com.pragma.home360.home.domain.ports.out.CityPersistencePort;
import com.pragma.home360.home.domain.ports.out.DepartmentPersistencePort;
import com.pragma.home360.home.domain.usecases.CityUseCase;
import com.pragma.home360.home.domain.utils.constants.DomainConstants;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;
import com.pragma.home360.home.unit.mocks.DepartmentMock;
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
        validDepartment = DepartmentMock.createDefaultDepartmentModel();
        validCity = new CityModel(1L, "Test City", "Test Description", validDepartment);
        validCity.setDepartmentId(validDepartment.getId()); // Mantener el ID para validaciones
    }

    @Nested
    @DisplayName("Tests para método saveCity")
    class SaveCityMethodTests {

        @Test
        @DisplayName("Debería guardar y retornar la ciudad cuando es válida")
        void saveCity_ValidCity_ShouldSaveAndReturnCity() {
            when(cityPersistencePort.existsCityByName(anyString())).thenReturn(false);
            when(departmentPersistencePort.getDepartmentById(any(Long.class))).thenReturn(Optional.of(validDepartment));
            when(cityPersistencePort.saveCity(any(CityModel.class))).thenReturn(validCity);

            CityModel result = cityUseCase.saveCity(validCity);

            assertNotNull(result);
            assertEquals(validCity.getId(), result.getId());
            assertEquals(validCity.getName(), result.getName());
            assertEquals(validCity.getDescription(), result.getDescription());
            assertEquals(validCity.getDepartmentId(), result.getDepartmentId());
            assertNotNull(result.getDepartment());
            assertEquals(validDepartment.getId(), result.getDepartment().getId());


            verify(cityPersistencePort).existsCityByName(validCity.getName().trim());
            verify(departmentPersistencePort).getDepartmentById(validCity.getDepartmentId());
            verify(cityPersistencePort).saveCity(cityModelCaptor.capture());
            assertEquals(validCity.getName(), cityModelCaptor.getValue().getName());
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando el nombre está vacío")
        void saveCity_EmptyName_ShouldThrowValidationException() {
            CityModel invalidCity = new CityModel(1L, "", "Test Description", validDepartment);
            invalidCity.setDepartmentId(validDepartment.getId());

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
            String longName = "a".repeat(DomainConstants.CITY_NAME_MAX_LENGTH + 1);
            CityModel invalidCity = new CityModel(1L, longName, "Test Description", validDepartment);
            invalidCity.setDepartmentId(validDepartment.getId());


            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> cityUseCase.saveCity(invalidCity)
            );

            assertEquals(DomainConstants.CITY_NAME_MAX_LENGTH_EXCEEDED, exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar ModelNotFoundException si el departamento no existe")
        void saveCity_DepartmentNotFound_ShouldThrowModelNotFoundException() {
            validCity.setDepartmentId(999L);
            when(cityPersistencePort.existsCityByName(anyString())).thenReturn(false);
            when(departmentPersistencePort.getDepartmentById(999L)).thenReturn(Optional.empty());

            ModelNotFoundException exception = assertThrows(
                    ModelNotFoundException.class,
                    () -> cityUseCase.saveCity(validCity)
            );
            assertEquals(String.format(DomainConstants.DEPARTMENT_NOT_FOUND, validCity.getDepartmentId()), exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Tests para método getCityById")
    class GetCityByIdMethodTests {

        @Test
        @DisplayName("Debería retornar la ciudad cuando existe")
        void getCityById_CityExists_ShouldReturnCity() {
            when(cityPersistencePort.getCityById(any(Long.class))).thenReturn(Optional.of(validCity));

            Optional<CityModel> result = cityUseCase.getCityById(validCity.getId());

            assertTrue(result.isPresent());
            assertEquals(validCity.getId(), result.get().getId());
            verify(cityPersistencePort).getCityById(validCity.getId());
        }

        @Test
        @DisplayName("Debería retornar Optional vacío cuando la ciudad no existe")
        void getCityById_CityDoesNotExist_ShouldReturnEmptyOptional() {
            Long cityId = 999L;
            when(cityPersistencePort.getCityById(cityId)).thenReturn(Optional.empty());

            Optional<CityModel> result = cityUseCase.getCityById(cityId);

            assertFalse(result.isPresent());
        }
    }

    @Nested
    @DisplayName("Tests para método getAllCities")
    class GetAllCitiesMethodTests {

        @Test
        @DisplayName("Debería retornar PagedResult de ciudades")
        void getAllCities_ShouldReturnPagedResultOfCities() {
            int page = 0;
            int size = 10;
            CityModel city1 = new CityModel(1L, "City 1", "Description 1", validDepartment);
            city1.setDepartmentId(validDepartment.getId());
            CityModel city2 = new CityModel(2L, "City 2", "Description 2", validDepartment);
            city2.setDepartmentId(validDepartment.getId());
            List<CityModel> cityList = Arrays.asList(city1, city2);
            PagedResult<CityModel> expectedPagedResult = new PagedResult<>(cityList, page, size, 2L, 1);

            when(cityPersistencePort.getAllCities(page, size)).thenReturn(expectedPagedResult);

            PagedResult<CityModel> result = cityUseCase.getAllCities(page, size);

            assertNotNull(result);
            assertEquals(expectedPagedResult.content().size(), result.content().size());
            assertEquals(expectedPagedResult, result);
            verify(cityPersistencePort).getAllCities(page, size);
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando page es negativo")
        void getAllCities_NegativePage_ShouldThrowValidationException() {
            int page = -1;
            int size = 10;

            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> cityUseCase.getAllCities(page, size)
            );
            assertEquals(DomainConstants.PAGINATION_PAGE_NEGATIVE, exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Tests para método getCityCount")
    class GetCityCountMethodTests {

        @Test
        @DisplayName("Debería retornar el conteo de ciudades")
        void getCityCount_ShouldReturnCount() {
            long expectedCount = 10L;
            when(cityPersistencePort.getCityCount()).thenReturn(expectedCount);

            long result = cityUseCase.getCityCount();

            assertEquals(expectedCount, result);
        }
    }
}
