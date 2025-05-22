package com.pragma.home360.home.integration.infrastructure.endpoints.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pragma.home360.home.application.dto.request.SavePropertyRequest;
import com.pragma.home360.home.application.dto.request.filters.PropertyFilterRequest;
import com.pragma.home360.home.application.dto.response.CategoryResponse;
import com.pragma.home360.home.application.dto.response.LocationResponse;
import com.pragma.home360.home.application.dto.response.PaginatedResponse;
import com.pragma.home360.home.application.dto.response.PropertyResponse;
import com.pragma.home360.home.application.services.PropertyService;
import com.pragma.home360.home.infrastructure.endpoints.rest.PropertyController;
import com.pragma.home360.home.infrastructure.entities.enums.PropertyPublicationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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


@WebMvcTest(PropertyController.class)
@DisplayName("Pruebas de Integración para PropertyController")
class PropertyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PropertyService propertyService;

    private ObjectMapper objectMapper;
    private PropertyResponse samplePropertyResponse;
    private SavePropertyRequest sampleSavePropertyRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        CategoryResponse categoryResponse = new CategoryResponse(1L, "Apartamento", "Unidad residencial");
        LocationResponse locationResponse = new LocationResponse(1L, "Calle Falsa 123", 6.217, -75.567, "Cerca al parque", null, null); // Asumiendo que Neighborhood y City pueden ser null o se mockean si son necesarios

        samplePropertyResponse = new PropertyResponse(
                1L,
                "Hermoso Apartamento en el Centro",
                "Un apartamento muy bien ubicado con excelentes vistas.",
                3,
                2,
                new BigDecimal("350000.00"),
                LocalDate.now().plusDays(5),
                PropertyPublicationStatus.PUBLICATION_PENDING,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now(),
                categoryResponse,
                locationResponse,
                List.of("image1.jpg", "image2.jpg")
        );

        sampleSavePropertyRequest = new SavePropertyRequest(
                "Hermoso Apartamento en el Centro",
                "Un apartamento muy bien ubicado con excelentes vistas.",
                3,
                2,
                new BigDecimal("350000.00"),
                LocalDate.now().plusDays(5),
                1, // categoryId
                1  // locationId
        );
    }

    @Test
    @DisplayName("POST /api/v1/properties - Debería crear una propiedad y retornar 201 Created")
    void createProperty_whenValidRequest_shouldReturn201Created() throws Exception {
        when(propertyService.saveProperty(any(SavePropertyRequest.class))).thenReturn(samplePropertyResponse);

        ResultActions resultActions = mockMvc.perform(post("/api/v1/properties")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleSavePropertyRequest)));

        resultActions.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(samplePropertyResponse.name())))
                .andExpect(jsonPath("$.price", is(samplePropertyResponse.price().doubleValue())));
    }

    @Test
    @DisplayName("POST /api/v1/properties - Debería retornar 400 Bad Request para una solicitud inválida")
    void createProperty_whenInvalidRequest_shouldReturn400BadRequest() throws Exception {
        SavePropertyRequest invalidRequest = new SavePropertyRequest(
                null, // Nombre nulo, debería ser inválido
                "Descripción válida",
                2,
                1,
                new BigDecimal("100000"),
                LocalDate.now().plusDays(1),
                1,
                1
        );
        // No necesitamos mockear el servicio aquí porque la validación de @Valid debería actuar primero

        ResultActions resultActions = mockMvc.perform(post("/api/v1/properties")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)));

        resultActions.andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("GET /api/v1/properties/{id} - Debería retornar la propiedad y 200 OK si existe")
    void getPropertyById_whenPropertyExists_shouldReturnPropertyAnd200OK() throws Exception {
        Long propertyId = 1L;
        when(propertyService.getPropertyById(propertyId)).thenReturn(samplePropertyResponse);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/properties/{id}", propertyId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(propertyId.intValue())))
                .andExpect(jsonPath("$.name", is(samplePropertyResponse.name())));
    }

    @Test
    @DisplayName("GET /api/v1/properties/{id} - Debería retornar 404 Not Found si la propiedad no existe")
    void getPropertyById_whenPropertyDoesNotExist_shouldReturn404NotFound() throws Exception {
        Long propertyId = 999L;
        when(propertyService.getPropertyById(propertyId)).thenReturn(null); // El servicio retorna null si no se encuentra

        ResultActions resultActions = mockMvc.perform(get("/api/v1/properties/{id}", propertyId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/v1/properties - Debería retornar una lista paginada de propiedades y 200 OK")
    void getAllProperties_shouldReturnPaginatedPropertiesAnd200OK() throws Exception {
        List<PropertyResponse> propertyList = Collections.singletonList(samplePropertyResponse);
        PaginatedResponse<PropertyResponse> paginatedResponse = new PaginatedResponse<>(
                propertyList, 0, 1, 1, 1L
        );

        when(propertyService.getAllProperties(any(PropertyFilterRequest.class))).thenReturn(paginatedResponse);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/properties")
                .param("page", "0")
                .param("size", "1")
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(samplePropertyResponse.id().intValue())))
                .andExpect(jsonPath("$.currentPage", is(0)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalElements", is(1)));
    }

    @Test
    @DisplayName("GET /api/v1/properties - Debería retornar una lista vacía si no hay propiedades y 200 OK")
    void getAllProperties_whenNoProperties_shouldReturnEmptyListAnd200OK() throws Exception {
        PaginatedResponse<PropertyResponse> emptyPaginatedResponse = new PaginatedResponse<>(
                Collections.emptyList(), 0, 10, 0, 0L
        );

        when(propertyService.getAllProperties(any(PropertyFilterRequest.class))).thenReturn(emptyPaginatedResponse);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/properties")
                .param("page", "0")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.totalElements", is(0)));
    }
}
