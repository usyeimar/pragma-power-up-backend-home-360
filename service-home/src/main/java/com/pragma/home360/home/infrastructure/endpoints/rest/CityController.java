package com.pragma.home360.home.infrastructure.endpoints.rest;


import com.pragma.home360.home.application.dto.response.PaginatedResponse;
import com.pragma.home360.home.application.dto.request.filters.FilterRequest;
import com.pragma.home360.home.application.dto.request.SaveCityRequest;
import com.pragma.home360.home.application.dto.response.CityResponse;
import com.pragma.home360.home.application.services.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cities")
@Tag(name = "Ciudades", description = "Gestión de ciudades")
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping
    @Operation(summary = "Crear una nueva ciudad",
            description = "Crea una nueva ciudad con nombre y descripción. Solo accesible para administradores.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Ciudad creada con éxito",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CityResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
                    @ApiResponse(responseCode = "403", description = "No tiene permisos para crear ciudades")
            })
    public ResponseEntity<CityResponse> createCity(@RequestBody SaveCityRequest request) {
        CityResponse response = cityService.saveCity(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar todas las ciudades",
            description = "Obtiene una lista paginada de todas las ciudades.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista recuperada con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginatedResponse.class)))
            })
    public ResponseEntity<PaginatedResponse<CityResponse>> getAllCities(@ParameterObject FilterRequest paginationRequest) {
        PaginatedResponse<CityResponse> response = cityService.getAllCities(paginationRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una ciudad por ID",
            description = "Obtiene una ciudad específica por su ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ciudad encontrada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CityResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Ciudad no encontrada")
            })
    public ResponseEntity<CityResponse> getCityById(@PathVariable Long id) {
        CityResponse response = cityService.getCityById(id);
        return ResponseEntity.ok(response);
    }
}