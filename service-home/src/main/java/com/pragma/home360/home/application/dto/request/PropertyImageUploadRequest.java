package com.pragma.home360.home.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record PropertyImageUploadRequest(
        @Schema(description = "Archivo de imagen a cargar")
        @NotNull(message = "El archivo de imagen no puede ser nulo.")
        MultipartFile imageFile,

        @Schema(description = "Descripción opcional de la imagen", example = "Vista frontal de la propiedad")
        String description,

        @Schema(description = "Indica si esta es la imagen principal de la propiedad", defaultValue = "false")
        Boolean isMainImage
) {
    public PropertyImageUploadRequest {
        if (isMainImage == null) {
            isMainImage = false;
        }
    }
}