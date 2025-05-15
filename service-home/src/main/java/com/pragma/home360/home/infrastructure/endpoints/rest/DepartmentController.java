package com.pragma.home360.home.infrastructure.endpoints.rest;


import com.pragma.home360.home.application.dto.response.PaginatedResponse;
import com.pragma.home360.home.application.dto.request.filters.FilterRequest;
import com.pragma.home360.home.application.dto.request.SaveDepartmentRequest;
import com.pragma.home360.home.application.dto.response.DepartmentResponse;
import com.pragma.home360.home.application.services.DepartmentService;
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
@RequestMapping("/api/v1/departments")
@Tag(name = "Departamentos", description = "Gestion de departamentos")
public class DepartmentController {

    private final DepartmentService departmentService;


    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo departamento", description = "Crea un nuevo departamento. Solo accesible para administradores.", responses = {@ApiResponse(responseCode = "201", description = "Departamento creado con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DepartmentResponse.class))), @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"), @ApiResponse(responseCode = "403", description = "No tiene permisos para crear departamentos"), @ApiResponse(responseCode = "409", description = "Ya existe un departamento con ese nombre")})
    public ResponseEntity<DepartmentResponse> createDepartment(@RequestBody SaveDepartmentRequest request) {
        DepartmentResponse response = departmentService.saveDepartment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar todos los departamentos", description = "Obtiene una lista paginada de todos los departamentos.", responses = {@ApiResponse(responseCode = "200", description = "Lista recuperada con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaginatedResponse.class)))})
    public ResponseEntity<PaginatedResponse<DepartmentResponse>> getAllDepartments(@ParameterObject FilterRequest paginationRequest) {

        PaginatedResponse<DepartmentResponse> response = departmentService.getAllDepartments(paginationRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un departamento por ID", description = "Obtiene un departamento específico por su ID.", responses = {@ApiResponse(responseCode = "200", description = "Departamento encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DepartmentResponse.class))), @ApiResponse(responseCode = "404", description = "Departamento no encontrado")})
    public ResponseEntity<DepartmentResponse> getDepartmentById(@PathVariable Long id) {
        DepartmentResponse response = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(response);
    }
}