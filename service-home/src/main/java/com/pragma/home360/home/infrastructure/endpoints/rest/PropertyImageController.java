package com.pragma.home360.home.infrastructure.endpoints.rest;


import com.pragma.home360.home.application.dto.request.PropertyImageUploadRequest;
import com.pragma.home360.home.application.dto.response.PropertyImageResponse;
import com.pragma.home360.home.application.services.PropertyImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/properties/{propertyId}/images")
@Tag(name = "Imágenes de Propiedades", description = "Gestión de imágenes para propiedades")
@RequiredArgsConstructor
public class PropertyImageController {

    private final PropertyImageService propertyImageService;

    @Operation(summary = "Cargar una imagen para una propiedad",
            description = "Sube un archivo de imagen y lo asocia a una propiedad específica.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Imagen cargada exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PropertyImageResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida o archivo no válido"),
                    @ApiResponse(responseCode = "404", description = "Propiedad no encontrada")
            })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PropertyImageResponse> uploadPropertyImage(
            @Parameter(description = "ID de la propiedad a la que se asociará la imagen", required = true)
            @PathVariable Long propertyId,
            @Parameter(description = "Archivo de imagen a cargar", required = true, content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestPart("imageFile") MultipartFile imageFile,
            @Parameter(description = "Descripción opcional de la imagen")
            @RequestPart(value = "description", required = false) String description,
            @Parameter(description = "Indica si esta es la imagen principal")
            @RequestPart(value = "isMainImage", required = false) Boolean isMainImage) {

        PropertyImageUploadRequest request = new PropertyImageUploadRequest(imageFile, description, isMainImage);
        PropertyImageResponse response = propertyImageService.uploadImageToProperty(propertyId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtener todas las imágenes de una propiedad",
            description = "Recupera una lista de todas las imágenes asociadas a una propiedad específica.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Imágenes recuperadas exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Propiedad no encontrada")
            })
    @GetMapping
    public ResponseEntity<List<PropertyImageResponse>> getPropertyImages(
            @Parameter(description = "ID de la propiedad", required = true)
            @PathVariable Long propertyId) {
        List<PropertyImageResponse> responses = propertyImageService.getImagesForProperty(propertyId);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Obtener una imagen específica por su ID",
            description = "Recupera los detalles de una imagen de propiedad específica.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Imagen encontrada"),
                    @ApiResponse(responseCode = "404", description = "Imagen no encontrada o no pertenece a la propiedad")
            })
    @GetMapping("/{imageId}")
    public ResponseEntity<PropertyImageResponse> getPropertyImageById(
            @Parameter(description = "ID de la propiedad", required = true) @PathVariable Long propertyId,
            @Parameter(description = "ID de la imagen a recuperar", required = true) @PathVariable Long imageId) {

        PropertyImageResponse response = propertyImageService.getImageById(imageId);
        if (!response.propertyId().equals(propertyId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Eliminar una imagen de propiedad",
            description = "Elimina una imagen específica por su ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Imagen eliminada exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Imagen no encontrada")
            })
    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deletePropertyImage(
            @Parameter(description = "ID de la propiedad (contextual, para la ruta)", required = true)
            @PathVariable Long propertyId, // Aunque no se usa directamente, es parte de la ruta RESTful
            @Parameter(description = "ID de la imagen a eliminar", required = true)
            @PathVariable Long imageId) {
        propertyImageService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Establecer una imagen como principal para una propiedad",
            description = "Marca una imagen específica como la imagen principal de la propiedad, desmarcando cualquier otra.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Imagen establecida como principal"),
                    @ApiResponse(responseCode = "404", description = "Propiedad o imagen no encontrada"),
                    @ApiResponse(responseCode = "400", description = "La imagen no pertenece a la propiedad")
            })
    @PostMapping("/{imageId}/set-main")
    public ResponseEntity<Void> setMainPropertyImage(
            @Parameter(description = "ID de la propiedad", required = true) @PathVariable Long propertyId,
            @Parameter(description = "ID de la imagen a establecer como principal", required = true) @PathVariable Long imageId) {
        propertyImageService.setMainImage(propertyId, imageId);
        return ResponseEntity.ok().build();
    }
}