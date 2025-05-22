package com.pragma.home360.home.integration.infrastructure.endpoints.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pragma.home360.home.application.dto.request.PropertyImageUploadRequest;
import com.pragma.home360.home.application.dto.response.PropertyImageResponse;
import com.pragma.home360.home.application.services.PropertyImageService;
import com.pragma.home360.home.infrastructure.endpoints.rest.PropertyImageController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;

@WebMvcTest(PropertyImageController.class)
@DisplayName("Pruebas de Integración para PropertyImageController")
class PropertyImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PropertyImageService propertyImageService;

    private PropertyImageResponse samplePropertyImageResponse;
    private MockMultipartFile sampleImageFile;
    private final Long samplePropertyId = 1L;
    private final Long sampleImageId = 1L;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        samplePropertyImageResponse = new PropertyImageResponse(sampleImageId, "/media/properties/1/test.jpg", "Test Image", false, samplePropertyId);
        sampleImageFile = new MockMultipartFile("imageFile", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test image content".getBytes());
    }

    @Test
    @DisplayName("POST /api/v1/properties/{propertyId}/images - Debería cargar una imagen y retornar 201 Created")
    void uploadPropertyImage_whenValidRequest_shouldReturn201Created() throws Exception {
        when(propertyImageService.uploadImageToProperty(eq(samplePropertyId), any(PropertyImageUploadRequest.class))).thenReturn(samplePropertyImageResponse);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/properties/{propertyId}/images", samplePropertyId).file(sampleImageFile).param("description", "Test Image Description").param("isMainImage", "false"));

        resultActions.andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.id", is(sampleImageId.intValue()))).andExpect(jsonPath("$.imageUrl", is(samplePropertyImageResponse.imageUrl())));
    }

    @Test
    @DisplayName("GET /api/v1/properties/{propertyId}/images - Debería retornar lista de imágenes y 200 OK")
    void getPropertyImages_shouldReturnListOfImagesAnd200OK() throws Exception {
        List<PropertyImageResponse> imageList = Collections.singletonList(samplePropertyImageResponse);
        when(propertyImageService.getImagesForProperty(samplePropertyId)).thenReturn(imageList);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/properties/{propertyId}/images", samplePropertyId).accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].id", is(sampleImageId.intValue())));
    }

    @Test
    @DisplayName("GET /api/v1/properties/{propertyId}/images/{imageId} - Debería retornar la imagen y 200 OK")
    void getPropertyImageById_whenImageExists_shouldReturnImageAnd200OK() throws Exception {
        when(propertyImageService.getImageById(sampleImageId)).thenReturn(samplePropertyImageResponse);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/properties/{propertyId}/images/{imageId}", samplePropertyId, sampleImageId).accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.id", is(sampleImageId.intValue()))).andExpect(jsonPath("$.propertyId", is(samplePropertyId.intValue())));
    }

    @Test
    @DisplayName("GET /api/v1/properties/{propertyId}/images/{imageId} - Debería retornar 404 si la imagen no pertenece a la propiedad")
    void getPropertyImageById_whenImageNotBelongsToProperty_shouldReturn404NotFound() throws Exception {
        PropertyImageResponse imageFromOtherProperty = new PropertyImageResponse(sampleImageId, "/media/properties/2/other.jpg", "Other Image", false, 2L);
        when(propertyImageService.getImageById(sampleImageId)).thenReturn(imageFromOtherProperty);
        ResultActions resultActions = mockMvc.perform(get("/api/v1/properties/{propertyId}/images/{imageId}", samplePropertyId, sampleImageId).accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("DELETE /api/v1/properties/{propertyId}/images/{imageId} - Debería eliminar la imagen y retornar 204 No Content")
    void deletePropertyImage_shouldReturn204NoContent() throws Exception {
        doNothing().when(propertyImageService).deleteImage(sampleImageId);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/properties/{propertyId}/images/{imageId}", samplePropertyId, sampleImageId));

        resultActions.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST /api/v1/properties/{propertyId}/images/{imageId}/set-main - Debería establecer imagen principal y retornar 200 OK")
    void setMainPropertyImage_shouldReturn200OK() throws Exception {
        doNothing().when(propertyImageService).setMainImage(samplePropertyId, sampleImageId);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/properties/{propertyId}/images/{imageId}/set-main", samplePropertyId, sampleImageId));

        resultActions.andExpect(status().isOk());
    }
}
