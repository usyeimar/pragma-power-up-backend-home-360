package com.pragma.home360.home.integration.infrastructure.endpoints.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pragma.home360.home.application.dto.request.SaveNeighborhoodRequest;
import com.pragma.home360.home.application.dto.request.filters.FilterRequest;
import com.pragma.home360.home.application.dto.response.CityResponse;
import com.pragma.home360.home.application.dto.response.DepartmentResponse;
import com.pragma.home360.home.application.dto.response.NeighborhoodResponse;
import com.pragma.home360.home.application.dto.response.PaginatedResponse;
import com.pragma.home360.home.application.services.NeighborhoodService;
import com.pragma.home360.home.infrastructure.endpoints.rest.NeighborHoodController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;

@WebMvcTest(NeighborHoodController.class) // Asegúrate que el controlador es NeighborHoodController
@DisplayName("Pruebas de Integración para NeighborhoodController")
class NeighborhoodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NeighborhoodService neighborhoodService;

    private ObjectMapper objectMapper;
    private NeighborhoodResponse sampleNeighborhoodResponse;
    private SaveNeighborhoodRequest sampleSaveNeighborhoodRequest;
    private CityResponse sampleCityResponse;
    private DepartmentResponse sampleDepartmentResponse;


    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        sampleDepartmentResponse = new DepartmentResponse(1L, "Antioquia", "Departamento de Antioquia");
        sampleCityResponse = new CityResponse(1L, "Medellín", "Capital de Antioquia", sampleDepartmentResponse);
        sampleNeighborhoodResponse = new NeighborhoodResponse(1L, "El Poblado", "Barrio exclusivo", sampleCityResponse);
        sampleSaveNeighborhoodRequest = new SaveNeighborhoodRequest("El Poblado", "Barrio exclusivo", 1L);
    }

    @Test
    @DisplayName("POST /api/v1/neighborhoods - Debería crear un barrio y retornar 201 Created")
    void createNeighborhood_whenValidRequest_shouldReturn201Created() throws Exception {
        when(neighborhoodService.saveNeighborhood(any(SaveNeighborhoodRequest.class))).thenReturn(sampleNeighborhoodResponse);

        ResultActions resultActions = mockMvc.perform(post("/api/v1/neighborhoods")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleSaveNeighborhoodRequest)));

        resultActions.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(sampleNeighborhoodResponse.name())))
                .andExpect(jsonPath("$.city.id", is(sampleCityResponse.id().intValue())));
    }

    @Test
    @DisplayName("GET /api/v1/neighborhoods/{id} - Debería retornar el barrio y 200 OK si existe")
    void getNeighborhoodById_whenNeighborhoodExists_shouldReturnNeighborhoodAnd200OK() throws Exception {
        Long neighborhoodId = 1L;
        when(neighborhoodService.getNeighborhoodById(neighborhoodId)).thenReturn(sampleNeighborhoodResponse);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/neighborhoods/{id}", neighborhoodId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(neighborhoodId.intValue())))
                .andExpect(jsonPath("$.name", is(sampleNeighborhoodResponse.name())));
    }

    @Test
    @DisplayName("GET /api/v1/neighborhoods/{id} - Debería retornar 404 Not Found si el barrio no existe")
    void getNeighborhoodById_whenNeighborhoodDoesNotExist_shouldReturn404NotFound() throws Exception {
        Long neighborhoodId = 999L;
        when(neighborhoodService.getNeighborhoodById(neighborhoodId)).thenReturn(null); // o lanzar ModelNotFoundException

        ResultActions resultActions = mockMvc.perform(get("/api/v1/neighborhoods/{id}", neighborhoodId)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("GET /api/v1/neighborhoods - Debería retornar una lista paginada de barrios y 200 OK")
    void getAllNeighborhoods_shouldReturnPaginatedNeighborhoodsAnd200OK() throws Exception {
        List<NeighborhoodResponse> neighborhoodList = Collections.singletonList(sampleNeighborhoodResponse);
        PaginatedResponse<NeighborhoodResponse> paginatedResponse = new PaginatedResponse<>(
                neighborhoodList, 0, 1, 1, 1L
        );

        when(neighborhoodService.getAllNeighborhoods(any(FilterRequest.class))).thenReturn(paginatedResponse);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/neighborhoods")
                .param("page", "0")
                .param("size", "1")
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(sampleNeighborhoodResponse.id().intValue())))
                .andExpect(jsonPath("$.currentPage", is(0)));
    }

    @Test
    @DisplayName("GET /api/v1/neighborhoods/by-city/{cityId} - Debería retornar barrios por ciudad y 200 OK")
    void getNeighborhoodsByCity_shouldReturnPaginatedNeighborhoodsAnd200OK() throws Exception {
        Long cityId = 1L;
        List<NeighborhoodResponse> neighborhoodList = Collections.singletonList(sampleNeighborhoodResponse);
        PaginatedResponse<NeighborhoodResponse> paginatedResponse = new PaginatedResponse<>(
                neighborhoodList, 0, 1, 1, 1L
        );

        when(neighborhoodService.getNeighborhoodsByCity(eq(cityId), any(FilterRequest.class))).thenReturn(paginatedResponse);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/neighborhoods/by-city/{cityId}", cityId)
                .param("page", "0")
                .param("size", "1")
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].city.id", is(cityId.intValue())));
    }
}
