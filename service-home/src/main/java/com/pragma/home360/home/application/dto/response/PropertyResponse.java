package com.pragma.home360.home.application.dto.response;

import com.pragma.home360.home.infrastructure.entities.enums.PropertyPublicationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record PropertyResponse(
        Long id,
        String name,
        String description,
        Integer numberOfRooms,
        Integer numberOfBathrooms,
        Double price,
        LocalDate activePublicationDate,
        PropertyPublicationStatus publicationStatus,
        LocalDateTime publicationCreationDate,
        Integer locationId,
        Integer categoryId,
        List<String> images
        ) {
}