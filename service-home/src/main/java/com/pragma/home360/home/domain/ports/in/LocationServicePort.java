package com.pragma.home360.home.domain.ports.in;

import com.pragma.home360.home.domain.model.LocationModel;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;

import java.util.Optional;

public interface LocationServicePort {

    LocationModel saveLocation(LocationModel location);

    PagedResult<LocationModel> searchLocations(String searchText, int page, int size, String sortBy, String sortDirection);

    Optional<LocationModel> getLocationById(Long id);
}