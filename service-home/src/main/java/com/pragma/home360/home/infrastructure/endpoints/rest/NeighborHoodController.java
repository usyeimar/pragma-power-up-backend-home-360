package com.pragma.home360.home.infrastructure.endpoints.rest;

import com.pragma.home360.home.application.dto.response.PaginatedResponse;
import com.pragma.home360.home.application.dto.request.SaveNeighborhoodRequest;
import com.pragma.home360.home.application.dto.request.filters.FilterRequest;
import com.pragma.home360.home.application.dto.response.NeighborhoodResponse;
import com.pragma.home360.home.application.services.NeighborhoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/neighborhoods")
@Tag(name = "Barrios", description = "Gestión de barrios")
@RequiredArgsConstructor
public class NeighborHoodController {

    private final NeighborhoodService neighborhoodService;

    @PostMapping
    @Operation(summary = "Crear un nuevo barrio",
            description = "Crea un nuevo barrio asociado a una ciudad existente. Solo accesible para administradores.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Barrio creado con éxito",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = NeighborhoodResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
                    @ApiResponse(responseCode = "403", description = "No tiene permisos para crear barrios"),
                    @ApiResponse(responseCode = "404", description = "Ciudad no encontrada"),
                    @ApiResponse(responseCode = "409", description = "Ya existe un barrio con ese nombre")
            })
    public ResponseEntity<NeighborhoodResponse> createNeighborhood(@Valid @RequestBody SaveNeighborhoodRequest request) {
        NeighborhoodResponse response = neighborhoodService.saveNeighborhood(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un barrio por ID",
            description = "Obtiene un barrio específico por su ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Barrio encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = NeighborhoodResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Barrio no encontrado")
            })
    public ResponseEntity<NeighborhoodResponse> getNeighborhoodById(@PathVariable Long id) {
        NeighborhoodResponse response = neighborhoodService.getNeighborhoodById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar todos los barrios",
            description = "Obtiene una lista paginada de todos los barrios.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista recuperada con éxito",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PaginatedResponse.class)))
            })
    public ResponseEntity<PaginatedResponse<NeighborhoodResponse>> getAllNeighborhoods(
            @ParameterObject FilterRequest filter) {
        PaginatedResponse<NeighborhoodResponse> response = neighborhoodService.getAllNeighborhoods(filter);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-city/{cityId}")
    @Operation(summary = "Listar barrios por ciudad",
            description = "Obtiene una lista paginada de barrios pertenecientes a una ciudad específica.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista recuperada con éxito",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PaginatedResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Ciudad no encontrada")
            })
    public ResponseEntity<PaginatedResponse<NeighborhoodResponse>> getNeighborhoodsByCity(
            @PathVariable Long cityId,
            @ParameterObject FilterRequest filter) {
        PaginatedResponse<NeighborhoodResponse> response = neighborhoodService.getNeighborhoodsByCity(cityId, filter);
        return ResponseEntity.ok(response);
    }
}