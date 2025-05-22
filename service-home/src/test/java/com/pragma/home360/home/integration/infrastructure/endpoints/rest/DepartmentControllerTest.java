package com.pragma.home360.home.integration.infrastructure.endpoints.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pragma.home360.home.application.dto.request.SaveDepartmentRequest;
import com.pragma.home360.home.application.dto.request.filters.FilterRequest;
import com.pragma.home360.home.application.dto.response.DepartmentResponse;
import com.pragma.home360.home.application.dto.response.PaginatedResponse;
import com.pragma.home360.home.application.services.DepartmentService;
import com.pragma.home360.home.infrastructure.endpoints.rest.DepartmentController;
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

@WebMvcTest(DepartmentController.class)
@DisplayName("Pruebas de Integración para DepartmentController")
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DepartmentService departmentService;

    private ObjectMapper objectMapper;
    private DepartmentResponse sampleDepartmentResponse;
    private SaveDepartmentRequest sampleSaveDepartmentRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        sampleDepartmentResponse = new DepartmentResponse(1L, "Antioquia", "Departamento de Antioquia");
        sampleSaveDepartmentRequest = new SaveDepartmentRequest("Antioquia", "Departamento de Antioquia");
    }

    @Test
    @DisplayName("POST /api/v1/departments - Debería crear un departamento y retornar 201 Created")
    void createDepartment_whenValidRequest_shouldReturn201Created() throws Exception {
        when(departmentService.saveDepartment(any(SaveDepartmentRequest.class))).thenReturn(sampleDepartmentResponse);

        ResultActions resultActions = mockMvc.perform(post("/api/v1/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleSaveDepartmentRequest)));

        resultActions.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(sampleDepartmentResponse.name())));
    }

    @Test
    @DisplayName("GET /api/v1/departments/{id} - Debería retornar el departamento y 200 OK si existe")
    void getDepartmentById_whenDepartmentExists_shouldReturnDepartmentAnd200OK() throws Exception {
        Long departmentId = 1L;
        when(departmentService.getDepartmentById(departmentId)).thenReturn(sampleDepartmentResponse);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/departments/{id}", departmentId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(departmentId.intValue())))
                .andExpect(jsonPath("$.name", is(sampleDepartmentResponse.name())));
    }

    @Test
    @DisplayName("GET /api/v1/departments/{id} - Debería retornar 404 Not Found si el departamento no existe")
    void getDepartmentById_whenDepartmentDoesNotExist_shouldReturn404NotFound() throws Exception {
        Long departmentId = 999L;
        when(departmentService.getDepartmentById(departmentId)).thenReturn(null);


        ResultActions resultActions = mockMvc.perform(get("/api/v1/departments/{id}", departmentId)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("GET /api/v1/departments - Debería retornar una lista paginada de departamentos y 200 OK")
    void getAllDepartments_shouldReturnPaginatedDepartmentsAnd200OK() throws Exception {
        List<DepartmentResponse> departmentList = Collections.singletonList(sampleDepartmentResponse);
        PaginatedResponse<DepartmentResponse> paginatedResponse = new PaginatedResponse<>(
                departmentList, 0, 1, 1, 1L
        );

        when(departmentService.getAllDepartments(any(FilterRequest.class))).thenReturn(paginatedResponse);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/departments")
                .param("page", "0")
                .param("size", "1")
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(sampleDepartmentResponse.id().intValue())))
                .andExpect(jsonPath("$.currentPage", is(0)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalElements", is(1)));
    }
}
