package com.pragma.home360.home.integration.infrastructure.endpoints.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pragma.home360.home.application.dto.request.SaveCategoryRequest;
import com.pragma.home360.home.application.dto.request.filters.CategoryFilterRequest;
import com.pragma.home360.home.application.dto.response.CategoryResponse;
import com.pragma.home360.home.application.dto.response.PaginatedResponse;
import com.pragma.home360.home.application.services.CategoryService;
import com.pragma.home360.home.infrastructure.endpoints.rest.CategoryController;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;

@WebMvcTest(CategoryController.class)
@DisplayName("Pruebas de Integración para CategoryController")
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    private ObjectMapper objectMapper;
    private CategoryResponse sampleCategoryResponse;
    private SaveCategoryRequest sampleSaveCategoryRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        sampleCategoryResponse = new CategoryResponse(1L, "Apartamento", "Unidad residencial en edificio");
        sampleSaveCategoryRequest = new SaveCategoryRequest("Apartamento", "Unidad residencial en edificio");
    }

    @Test
    @DisplayName("POST /api/v1/categories - Debería crear una categoría y retornar 201 Created")
    void createCategory_whenValidRequest_shouldReturn201Created() throws Exception {
        when(categoryService.save(any(SaveCategoryRequest.class))).thenReturn(sampleCategoryResponse);

        ResultActions resultActions = mockMvc.perform(post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleSaveCategoryRequest)));

        resultActions.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(sampleCategoryResponse.name())))
                .andExpect(jsonPath("$.description", is(sampleCategoryResponse.description())));
    }

    @Test
    @DisplayName("GET /api/v1/categories - Debería retornar una lista paginada de categorías y 200 OK")
    void getAllCategories_shouldReturnPaginatedCategoriesAnd200OK() throws Exception {
        List<CategoryResponse> categoryList = Collections.singletonList(sampleCategoryResponse);
        PaginatedResponse<CategoryResponse> paginatedResponse = new PaginatedResponse<>(
                categoryList, 0, 1, 1, 1L
        );
        CategoryFilterRequest filterRequest = new CategoryFilterRequest(0, 1, "id", Sort.Direction.ASC);

        when(categoryService.getCategories(any(CategoryFilterRequest.class))).thenReturn(paginatedResponse);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/categories")
                .param("page", String.valueOf(filterRequest.page()))
                .param("size", String.valueOf(filterRequest.size()))
                .param("sortField", filterRequest.sortField())
                .param("direction", filterRequest.direction().name())
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(sampleCategoryResponse.id().intValue())))
                .andExpect(jsonPath("$.currentPage", is(0)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalElements", is(1)));
    }

    @Test
    @DisplayName("GET /api/v1/categories - Debería retornar una lista vacía si no hay categorías y 200 OK")
    void getAllCategories_whenNoCategories_shouldReturnEmptyListAnd200OK() throws Exception {
        PaginatedResponse<CategoryResponse> emptyPaginatedResponse = new PaginatedResponse<>(
                Collections.emptyList(), 0, 10, 0, 0L
        );
        CategoryFilterRequest filterRequest = new CategoryFilterRequest(0, 10, "id", Sort.Direction.ASC);

        when(categoryService.getCategories(any(CategoryFilterRequest.class))).thenReturn(emptyPaginatedResponse);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/categories")
                .param("page", String.valueOf(filterRequest.page()))
                .param("size", String.valueOf(filterRequest.size()))
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.totalElements", is(0)));
    }
}
