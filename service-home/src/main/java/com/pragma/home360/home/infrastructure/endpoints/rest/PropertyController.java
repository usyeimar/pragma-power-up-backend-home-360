package com.pragma.home360.home.infrastructure.endpoints.rest;


import com.pragma.home360.home.application.dto.request.SavePropertyRequest;
import com.pragma.home360.home.application.dto.request.filters.PropertyFilterRequest;
import com.pragma.home360.home.application.dto.response.PropertyResponse;
import com.pragma.home360.home.application.services.PropertyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/properties")
@Tag(name = "Propiedades", description = "Gestión de propiedades")
@RequiredArgsConstructor
public class PropertyController {

    protected final PropertyService propertyService;

    @PostMapping
    public PropertyResponse createProperty(@RequestBody @Valid SavePropertyRequest request) {
        return propertyService.saveProperty(request);
    }

    @GetMapping
    public List<PropertyResponse> getAllProperties(@ParameterObject @Valid PropertyFilterRequest propertyFilterRequest) {
        return propertyService.getAllProperties(propertyFilterRequest);
    }

}
