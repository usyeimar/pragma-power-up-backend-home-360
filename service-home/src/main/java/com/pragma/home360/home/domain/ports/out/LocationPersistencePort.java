package com.pragma.home360.home.domain.ports.out;

import com.pragma.home360.home.domain.model.LocationModel;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;

import java.util.Optional;

public interface LocationPersistencePort {
    LocationModel saveLocation(LocationModel location);

    Optional<LocationModel> getLocationById(Long id);

    PagedResult<LocationModel> searchLocations(String normalizedText, int page, int size, String sortBy, String sortDirection);

}