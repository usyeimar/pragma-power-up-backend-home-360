package com.pragma.home360.home.integration.infrastructure.endpoints.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pragma.home360.home.application.dto.request.SaveCityRequest;
import com.pragma.home360.home.application.dto.request.filters.FilterRequest;
import com.pragma.home360.home.application.dto.response.CityResponse;
import com.pragma.home360.home.application.dto.response.DepartmentResponse;
import com.pragma.home360.home.application.dto.response.PaginatedResponse;
import com.pragma.home360.home.application.services.CityService;
import com.pragma.home360.home.infrastructure.endpoints.rest.CityController;
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

@WebMvcTest(CityController.class)
@DisplayName("Pruebas de Integración para CityController")
class CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CityService cityService;

    private ObjectMapper objectMapper;
    private CityResponse sampleCityResponse;
    private SaveCityRequest sampleSaveCityRequest;
    private DepartmentResponse sampleDepartmentResponse;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        sampleDepartmentResponse = new DepartmentResponse(1L, "Antioquia", "Departamento de Antioquia");
        sampleCityResponse = new CityResponse(1L, "Medellín", "Capital de Antioquia", sampleDepartmentResponse);
        sampleSaveCityRequest = new SaveCityRequest("Medellín", "Capital de Antioquia", 1L);
    }

    @Test
    @DisplayName("POST /api/v1/cities - Debería crear una ciudad y retornar 201 Created")
    void createCity_whenValidRequest_shouldReturn201Created() throws Exception {
        when(cityService.saveCity(any(SaveCityRequest.class))).thenReturn(sampleCityResponse);

        ResultActions resultActions = mockMvc.perform(post("/api/v1/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleSaveCityRequest)));

        resultActions.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(sampleCityResponse.name())))
                .andExpect(jsonPath("$.department.id", is(sampleDepartmentResponse.id().intValue())));
    }

    @Test
    @DisplayName("GET /api/v1/cities/{id} - Debería retornar la ciudad y 200 OK si existe")
    void getCityById_whenCityExists_shouldReturnCityAnd200OK() throws Exception {
        Long cityId = 1L;
        when(cityService.getCityById(cityId)).thenReturn(sampleCityResponse);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/cities/{id}", cityId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(cityId.intValue())))
                .andExpect(jsonPath("$.name", is(sampleCityResponse.name())));
    }

    @Test
    @DisplayName("GET /api/v1/cities/{id} - Debería retornar 404 Not Found si la ciudad no existe")
    void getCityById_whenCityDoesNotExist_shouldReturn404NotFound() throws Exception {
        Long cityId = 999L;
        when(cityService.getCityById(cityId)).thenReturn(null);
        ResultActions resultActions = mockMvc.perform(get("/api/v1/cities/{id}", cityId)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/v1/cities - Debería retornar una lista paginada de ciudades y 200 OK")
    void getAllCities_shouldReturnPaginatedCitiesAnd200OK() throws Exception {
        List<CityResponse> cityList = Collections.singletonList(sampleCityResponse);
        PaginatedResponse<CityResponse> paginatedResponse = new PaginatedResponse<>(
                cityList, 0, 1, 1, 1L
        );

        when(cityService.getAllCities(any(FilterRequest.class))).thenReturn(paginatedResponse);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/cities")
                .param("page", "0")
                .param("size", "1")
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(sampleCityResponse.id().intValue())))
                .andExpect(jsonPath("$.currentPage", is(0)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalElements", is(1)));
    }
}
