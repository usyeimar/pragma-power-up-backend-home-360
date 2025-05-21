package com.pragma.home360.home.infrastructure.endpoints.rest;

import com.pragma.home360.home.application.dto.request.SavePropertyRequest;
import com.pragma.home360.home.application.dto.request.filters.PropertyFilterRequest;
import com.pragma.home360.home.application.dto.response.PaginatedResponse;
import com.pragma.home360.home.application.dto.response.PropertyResponse;
import com.pragma.home360.home.application.services.PropertyService;
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
@RequestMapping("/api/v1/properties")
@Tag(name = "Propiedades", description = "Gestión de propiedades")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    @Operation(summary = "Crear una nueva propiedad",
            description = "Crea una nueva propiedad en el sistema.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Propiedad creada exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PropertyResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida")
            })
    @PostMapping
    public ResponseEntity<PropertyResponse> createProperty(@RequestBody @Valid SavePropertyRequest request) {
        PropertyResponse response = propertyService.saveProperty(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtener una propiedad por su ID",
            description = "Recupera los detalles de una propiedad específica usando su ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Propiedad encontrada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PropertyResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Propiedad no encontrada")
            })
    @GetMapping("/{id}")
    public ResponseEntity<PropertyResponse> getPropertyById(@PathVariable Long id) {
        PropertyResponse response = propertyService.getPropertyById(id);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Listar todas las propiedades con filtros y paginación",
            description = "Obtiene una lista paginada de propiedades, con opción de aplicar filtros.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Propiedades listadas exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PaginatedResponse.class)))
            })
    @GetMapping
    public ResponseEntity<PaginatedResponse<PropertyResponse>> getAllProperties(
            @ParameterObject @Valid PropertyFilterRequest propertyFilterRequest) {
        PaginatedResponse<PropertyResponse> response = propertyService.getAllProperties(propertyFilterRequest);
        return ResponseEntity.ok(response);
    }
}
