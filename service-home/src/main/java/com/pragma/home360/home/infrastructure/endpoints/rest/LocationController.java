package com.pragma.home360.home.infrastructure.endpoints.rest;

import com.pragma.home360.home.application.dto.response.PaginatedResponse;
import com.pragma.home360.home.application.dto.request.LocationSearchRequest;
import com.pragma.home360.home.application.dto.request.SaveLocationRequest;
import com.pragma.home360.home.application.dto.response.LocationResponse;
import com.pragma.home360.home.application.dto.response.LocationSearchResponse;
import com.pragma.home360.home.application.services.LocationService;
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

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/locations")
@Tag(name = "Ubicaciones", description = "Gestión de ubicaciones")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @PostMapping
    @Operation(summary = "Crear una nueva ubicación",
            description = "Crea una nueva ubicación con su barrio asociado.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Ubicación creada con éxito",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LocationResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
                    @ApiResponse(responseCode = "404", description = "Barrio no encontrado")
            })
    public ResponseEntity<LocationResponse> createLocation(@RequestBody SaveLocationRequest request) {
        LocationResponse response = locationService.saveLocation(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una ubicación por ID",
            description = "Obtiene una ubicación específica por su ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ubicación encontrada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LocationResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Ubicación no encontrada")
            })
    public ResponseEntity<LocationResponse> getLocationById(@PathVariable Long id) {
        Optional<LocationResponse> response = locationService.getLocationById(id);
        return response.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("")
    @Operation(summary = "Buscar ubicaciones",
            description = "Busca ubicaciones por texto en nombre de ciudad o departamento. " +
                    "No discrimina entre mayúsculas, minúsculas, tildes, etc. " +
                    "Permite ordenar ascendente o descendentemente por ciudad o departamento. " +
                    "El resultado está paginado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Búsqueda realizada con éxito",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PaginatedResponse.class)))
            })
    public ResponseEntity<PaginatedResponse<LocationSearchResponse>> searchLocations(
            @ParameterObject @Valid LocationSearchRequest request) {
        PaginatedResponse<LocationSearchResponse> response = locationService.searchLocations(request);
        return ResponseEntity.ok(response);
    }
}