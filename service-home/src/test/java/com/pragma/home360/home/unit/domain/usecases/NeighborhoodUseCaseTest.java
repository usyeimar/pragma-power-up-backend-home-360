package com.pragma.home360.home.unit.domain.usecases;

import com.pragma.home360.home.domain.exceptions.AlreadyExistsException;
import com.pragma.home360.home.domain.exceptions.ModelNotFoundException;
import com.pragma.home360.home.domain.exceptions.ValidationException;
import com.pragma.home360.home.domain.model.FilterModel;
import com.pragma.home360.home.domain.model.NeighborhoodModel;
import com.pragma.home360.home.domain.model.CityModel;
import com.pragma.home360.home.domain.model.DepartmentModel;
import com.pragma.home360.home.domain.ports.out.CityPersistencePort;
import com.pragma.home360.home.domain.ports.out.NeighborhoodPersistencePort;
import com.pragma.home360.home.domain.usecases.NeighborhoodUseCase;
import com.pragma.home360.home.domain.utils.constants.DomainConstants;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;
import com.pragma.home360.home.unit.mocks.FilterRequestMock;
import com.pragma.home360.home.unit.mocks.NeighborhoodMock;
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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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
    private DepartmentModel mockDepartment;
    private FilterModel validFilter;

    @BeforeEach
    void setUp() {
        mockDepartment = DepartmentMock.createDefaultDepartmentModel();
        mockCity = new CityModel(1L, "Test City", "Test Description", mockDepartment);
        validNeighborhood = NeighborhoodMock.createNeighborhoodModel(1L, "Test Neighborhood", "Test Desc", mockCity);
        validFilter = FilterRequestMock.createDefaultFilterRequest();
    }

    @Nested
    @DisplayName("Tests para método getNeighborhoodsByCity")
    class GetNeighborhoodsByCityMethodTests {

        @Test
        @DisplayName("Debería retornar lista paginada de barrios por ciudad")
        void getNeighborhoodsByCity_ValidCityId_ShouldReturnPaginatedNeighborhoods() {
            Long cityId = 1L;
            List<NeighborhoodModel> neighborhoodList = Arrays.asList(
                    validNeighborhood,
                    NeighborhoodMock.createNeighborhoodModel(2L, "Otro Barrio", "Desc Otro", mockCity)
            );

            PagedResult<NeighborhoodModel> expectedResponse = new PagedResult<>(
                    neighborhoodList,
                    validFilter.page(),
                    validFilter.size(),
                    2L,
                    1
            );

            when(cityPersistencePort.getCityById(cityId)).thenReturn(Optional.of(mockCity));
            when(neighborhoodPersistencePort.getNeighborhoodsByCity(eq(cityId), any(FilterModel.class)))
                    .thenReturn(expectedResponse);

            PagedResult<NeighborhoodModel> result = neighborhoodUseCase.getNeighborhoodsByCity(cityId, validFilter);

            assertNotNull(result);
            assertEquals(expectedResponse.content().size(), result.content().size());
            verify(cityPersistencePort).getCityById(cityId);
            verify(neighborhoodPersistencePort).getNeighborhoodsByCity(eq(cityId), filterCaptor.capture());
            assertEquals(validFilter.page(), filterCaptor.getValue().page());
        }


        @Test
        @DisplayName("Debería lanzar ValidationException cuando page es negativo")
        void getNeighborhoodsByCity_NegativePage_ShouldThrowValidationException() {
            Long cityId = 1L;
            FilterModel invalidFilter = FilterRequestMock.createFilterRequestWithNegativePage();
            when(cityPersistencePort.getCityById(cityId)).thenReturn(Optional.of(mockCity));

            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> neighborhoodUseCase.getNeighborhoodsByCity(cityId, invalidFilter)
            );

            assertEquals(DomainConstants.PAGINATION_PAGE_NEGATIVE, exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar ModelNotFoundException si la ciudad no existe al buscar barrios por ciudad")
        void getNeighborhoodsByCity_CityNotFound_ShouldThrowModelNotFoundException() {
            Long nonExistentCityId = 999L;
            when(cityPersistencePort.getCityById(nonExistentCityId)).thenReturn(Optional.empty());

            ModelNotFoundException exception = assertThrows(
                    ModelNotFoundException.class,
                    () -> neighborhoodUseCase.getNeighborhoodsByCity(nonExistentCityId, validFilter)
            );
            assertEquals(String.format(DomainConstants.CITY_NOT_FOUND, nonExistentCityId), exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Tests para método existsNeighborhoodByName")
    class ExistsNeighborhoodByNameMethodTests {

        @Test
        @DisplayName("Debería retornar true cuando el barrio existe")
        void existsNeighborhoodByName_NeighborhoodExists_ShouldReturnTrue() {
            String neighborhoodName = "Existing Neighborhood";
            when(neighborhoodPersistencePort.existsNeighborhoodByName(neighborhoodName)).thenReturn(true);
            boolean result = neighborhoodUseCase.existsNeighborhoodByName(neighborhoodName);
            assertTrue(result);
        }

        @Test
        @DisplayName("Debería retornar false cuando el barrio no existe")
        void existsNeighborhoodByName_NeighborhoodDoesNotExist_ShouldReturnFalse() {
            String neighborhoodName = "Non-existing Neighborhood";
            when(neighborhoodPersistencePort.existsNeighborhoodByName(neighborhoodName)).thenReturn(false);
            boolean result = neighborhoodUseCase.existsNeighborhoodByName(neighborhoodName);
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("Tests para método saveNeighborhood")
    class SaveNeighborhoodMethodTests {

        @Test
        @DisplayName("Debería guardar y retornar el barrio cuando es válido")
        void saveNeighborhood_ValidNeighborhood_ShouldSaveAndReturnNeighborhood() {
            when(neighborhoodPersistencePort.existsNeighborhoodByName(anyString())).thenReturn(false);
            when(cityPersistencePort.getCityById(any(Long.class))).thenReturn(Optional.of(mockCity));
            when(neighborhoodPersistencePort.saveNeighborhood(any(NeighborhoodModel.class))).thenReturn(validNeighborhood);

            NeighborhoodModel result = neighborhoodUseCase.saveNeighborhood(validNeighborhood);

            assertNotNull(result);
            assertEquals(validNeighborhood.getId(), result.getId());
            assertEquals(validNeighborhood.getName(), result.getName());
            assertNotNull(result.getCity());
            assertEquals(mockCity.getId(), result.getCity().getId());


            verify(neighborhoodPersistencePort).existsNeighborhoodByName(validNeighborhood.getName().trim());
            verify(cityPersistencePort).getCityById(validNeighborhood.getCity().getId());
            verify(neighborhoodPersistencePort).saveNeighborhood(neighborhoodModelCaptor.capture());
            assertEquals(validNeighborhood.getName(), neighborhoodModelCaptor.getValue().getName());
        }

        @Test
        @DisplayName("Debería lanzar ValidationException cuando el nombre está vacío")
        void saveNeighborhood_EmptyName_ShouldThrowValidationException() {
            NeighborhoodModel invalidNeighborhood = NeighborhoodMock.createNeighborhoodModelWithEmptyName();
            invalidNeighborhood.setCity(mockCity);


            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> neighborhoodUseCase.saveNeighborhood(invalidNeighborhood)
            );
            assertEquals(DomainConstants.NEIGHBORHOOD_NAME_CANNOT_BE_EMPTY, exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar AlreadyExistsException si el nombre del barrio ya existe")
        void saveNeighborhood_NameAlreadyExists_ShouldThrowAlreadyExistsException() {
            when(neighborhoodPersistencePort.existsNeighborhoodByName(validNeighborhood.getName().trim())).thenReturn(true);

            AlreadyExistsException exception = assertThrows(
                    AlreadyExistsException.class,
                    () -> neighborhoodUseCase.saveNeighborhood(validNeighborhood)
            );
            assertEquals(String.format(DomainConstants.NEIGHBORHOOD_NAME_ALREADY_EXISTS, validNeighborhood.getName()), exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar ModelNotFoundException si la ciudad no existe")
        void saveNeighborhood_CityNotFound_ShouldThrowModelNotFoundException() {
            when(neighborhoodPersistencePort.existsNeighborhoodByName(anyString())).thenReturn(false);
            when(cityPersistencePort.getCityById(validNeighborhood.getCity().getId())).thenReturn(Optional.empty());

            ModelNotFoundException exception = assertThrows(
                    ModelNotFoundException.class,
                    () -> neighborhoodUseCase.saveNeighborhood(validNeighborhood)
            );
            assertEquals(String.format(DomainConstants.CITY_NOT_FOUND, validNeighborhood.getCity().getId()), exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Tests para método getNeighborhoodById")
    class GetNeighborhoodByIdMethodTests {

        @Test
        @DisplayName("Debería retornar el barrio cuando existe")
        void getNeighborhoodById_NeighborhoodExists_ShouldReturnNeighborhood() {
            when(neighborhoodPersistencePort.getNeighborhoodById(any(Long.class))).thenReturn(Optional.of(validNeighborhood));
            NeighborhoodModel result = neighborhoodUseCase.getNeighborhoodById(validNeighborhood.getId());

            assertEquals(validNeighborhood.getId(), result.getId());
        }

        @Test
        @DisplayName("Debería retornar ModelNotFoundException si el barrio no existe")
        void getNeighborhoodById_NeighborhoodDoesNotExist_ShouldReturnEmptyOptional() {
            Long neighborhoodId = 999L;
            when(neighborhoodPersistencePort.getNeighborhoodById(neighborhoodId)).thenReturn(Optional.empty());
            assertThrows(ModelNotFoundException.class,
                    () -> neighborhoodUseCase.getNeighborhoodById(neighborhoodId),
                    String.format(DomainConstants.NEIGHBORHOOD_NOT_FOUND, neighborhoodId)
            );
        }
    }

    @Nested
    @DisplayName("Tests para método getAllNeighborhoods")
    class GetAllNeighborhoodsMethodTests {

        @Test
        @DisplayName("Debería retornar lista paginada de barrios")
        void getAllNeighborhoods_ShouldReturnPaginatedNeighborhoods() {
            List<NeighborhoodModel> neighborhoodList = Arrays.asList(
                    validNeighborhood,
                    NeighborhoodMock.createNeighborhoodModel(2L, "Otro Barrio", "Desc", mockCity)
            );
            PagedResult<NeighborhoodModel> expectedResponse = new PagedResult<>(
                    neighborhoodList,
                    validFilter.page(),
                    validFilter.size(),
                    2L,
                    1
            );
            when(neighborhoodPersistencePort.getAllNeighborhoods(any(FilterModel.class))).thenReturn(expectedResponse);

            PagedResult<NeighborhoodModel> result = neighborhoodUseCase.getAllNeighborhoods(validFilter);

            assertNotNull(result);
            assertEquals(expectedResponse.content().size(), result.content().size());
        }
    }
}
