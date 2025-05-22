package com.pragma.home360.home.integration.infrastructure.endpoints.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pragma.home360.home.application.dto.request.LocationSearchRequest;
import com.pragma.home360.home.application.dto.request.SaveLocationRequest;
import com.pragma.home360.home.application.dto.response.*;
import com.pragma.home360.home.application.services.LocationService;
import com.pragma.home360.home.infrastructure.endpoints.rest.LocationController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;

@WebMvcTest(LocationController.class)
@DisplayName("Pruebas de Integración para LocationController")
class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LocationService locationService;

    private ObjectMapper objectMapper;
    private LocationResponse sampleLocationResponse;
    private SaveLocationRequest sampleSaveLocationRequest;
    private LocationSearchResponse sampleLocationSearchResponse;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        DepartmentResponse dept = new DepartmentResponse(1L, "Antioquia", "Departamento");
        CityResponse city = new CityResponse(1L, "Medellín", "Ciudad Capital", dept);
        NeighborhoodResponse neighborhood = new NeighborhoodResponse(1L, "El Poblado", "Barrio exclusivo", city);

        sampleLocationResponse = new LocationResponse(1L, "Calle 10 #43A-50", 6.209, -75.570, "Parque Lleras", neighborhood, city);
        sampleSaveLocationRequest = new SaveLocationRequest(6.209, -75.570, "Calle 10 #43A-50", "Parque Lleras", 1L, 1L);
        sampleLocationSearchResponse = new LocationSearchResponse(1L, "Calle 10 #43A-50", 6.209, -75.570, "Parque Lleras", neighborhood, city);

    }

    @Test
    @DisplayName("POST /api/v1/locations - Debería crear una ubicación y retornar 201 Created")
    void createLocation_whenValidRequest_shouldReturn201Created() throws Exception {
        when(locationService.saveLocation(any(SaveLocationRequest.class))).thenReturn(sampleLocationResponse);

        ResultActions resultActions = mockMvc.perform(post("/api/v1/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleSaveLocationRequest)));

        resultActions.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.address", is(sampleLocationResponse.address())));
    }

    @Test
    @DisplayName("GET /api/v1/locations/{id} - Debería retornar la ubicación y 200 OK si existe")
    void getLocationById_whenLocationExists_shouldReturnLocationAnd200OK() throws Exception {
        Long locationId = 1L;
        when(locationService.getLocationById(locationId)).thenReturn(Optional.of(sampleLocationResponse));

        ResultActions resultActions = mockMvc.perform(get("/api/v1/locations/{id}", locationId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(locationId.intValue())))
                .andExpect(jsonPath("$.address", is(sampleLocationResponse.address())));
    }

    @Test
    @DisplayName("GET /api/v1/locations/{id} - Debería retornar 404 Not Found si la ubicación no existe")
    void getLocationById_whenLocationDoesNotExist_shouldReturn404NotFound() throws Exception {
        Long locationId = 999L;
        when(locationService.getLocationById(locationId)).thenReturn(Optional.empty());

        ResultActions resultActions = mockMvc.perform(get("/api/v1/locations/{id}", locationId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/v1/locations - Debería retornar una lista paginada de ubicaciones buscadas y 200 OK")
    void searchLocations_shouldReturnPaginatedLocationsAnd200OK() throws Exception {
        List<LocationSearchResponse> locationList = Collections.singletonList(sampleLocationSearchResponse);
        PaginatedResponse<LocationSearchResponse> paginatedResponse = new PaginatedResponse<>(
                locationList, 0, 1, 1, 1L
        );
        LocationSearchRequest searchRequest = new LocationSearchRequest("Poblado", 0,1,"city", Sort.Direction.ASC);


        when(locationService.searchLocations(any(LocationSearchRequest.class))).thenReturn(paginatedResponse);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/locations")
                .param("searchText", searchRequest.searchText())
                .param("page", String.valueOf(searchRequest.page()))
                .param("size", String.valueOf(searchRequest.size()))
                .param("sortBy", searchRequest.sortBy())
                .param("direction", searchRequest.direction().name())
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(sampleLocationSearchResponse.id().intValue())))
                .andExpect(jsonPath("$.currentPage", is(0)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalElements", is(1)));
    }
}
