package com.pragma.home360.home.infrastructure.endpoints.rest;

import com.pragma.home360.home.application.dto.request.SaveCategoryRequest;
import com.pragma.home360.home.application.dto.request.filters.CategoryFilterRequest;
import com.pragma.home360.home.application.dto.response.CategoryResponse;
import com.pragma.home360.home.application.dto.response.PaginatedResponse;
import com.pragma.home360.home.application.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Categorias", description = "Gestión de categorías de inmuebles")
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "Crear una categoría", description = "Crea una nueva categoría en el sistema")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Categoría creada correctamente"), @ApiResponse(responseCode = "400", description = "Petición inválida", content = @Content), @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)})
    @PostMapping()
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody SaveCategoryRequest saveCategoryRequest) {
        CategoryResponse category = categoryService.save(saveCategoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);

    }


    @Operation(summary = "Listar categorías", description = "Lista todas las categorías del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categorías listadas correctamente"),
            @ApiResponse(responseCode = "400", description = "Petición inválida", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping()
    public ResponseEntity<PaginatedResponse<CategoryResponse>> getAllCategory(
            @ParameterObject CategoryFilterRequest filter
    ) {

        PaginatedResponse<CategoryResponse> categories = categoryService.getCategories(filter);

        return ResponseEntity.ok(categories);
    }
}
