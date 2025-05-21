package com.pragma.home360.home.application.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta para una imagen de propiedad")
public record PropertyImageResponse(
        @Schema(description = "ID de la imagen", example = "1")
        Long id,

        @Schema(description = "URL de la imagen", example = "/media/properties/1/image_name.jpg")
        String imageUrl,

        @Schema(description = "Descripción de la imagen", example = "Fachada principal")
        String description,

        @Schema(description = "Indica si es la imagen principal", example = "true")
        Boolean isMainImage,

        @Schema(description = "ID de la propiedad a la que pertenece la imagen", example = "101")
        Long propertyId
) {}