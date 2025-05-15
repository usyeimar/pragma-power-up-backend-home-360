package com.pragma.home360.home.domain.usecases;

import com.pragma.home360.home.domain.model.LocationModel;
import com.pragma.home360.home.domain.ports.in.LocationServicePort;
import com.pragma.home360.home.domain.ports.out.CityPersistencePort;
import com.pragma.home360.home.domain.ports.out.LocationPersistencePort;
import com.pragma.home360.home.domain.ports.out.NeighborhoodPersistencePort;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;

import java.text.Normalizer;
import java.util.Optional;

import static com.pragma.home360.home.domain.utils.constants.DomainConstants.*;
import static com.pragma.home360.home.domain.utils.constants.Validator.*;

public class LocationUseCase implements LocationServicePort {

    private final LocationPersistencePort locationPersistencePort;
    private final NeighborhoodPersistencePort neighborhoodPersistencePort;
    private final CityPersistencePort cityPersistencePort;

    public LocationUseCase(LocationPersistencePort locationPersistencePort,
                           NeighborhoodPersistencePort neighborhoodPersistencePort
            , CityPersistencePort cityPersistencePort) {
        this.locationPersistencePort = locationPersistencePort;
        this.neighborhoodPersistencePort = neighborhoodPersistencePort;
        this.cityPersistencePort = cityPersistencePort;
    }

    @Override
    public LocationModel saveLocation(LocationModel location) {
        validateCustom(
                neighborhoodId -> neighborhoodPersistencePort
                        .getNeighborhoodById(neighborhoodId)
                        .isPresent(),
                location.getNeighborhoodId(),
                String.format(NEIGHBORHOOD_NOT_FOUND, location.getNeighborhoodId()),
                null
        );

        if (location.getCityId() != null) {
            validateCustom(
                    cityId -> cityPersistencePort
                            .getCityById(cityId)
                            .isPresent(),
                    location.getCityId(),
                    String.format(CITY_NOT_FOUND, location.getCityId()),
                    null

            );
        }

        return locationPersistencePort.saveLocation(location);
    }

    @Override
    public PagedResult<LocationModel> searchLocations(String searchText, int page, int size, String sortBy, String sortDirection) {
        String normalizedText = searchText != null ? normalizeText(searchText) : "";

        validateCustom(p -> p >= INITIAL_PAGE, page, PAGINATION_PAGE_NEGATIVE, RuntimeException.class.getName());
        validateCustom(s -> s >= MIN_PAGE_SIZE && s <= MAX_PAGE_SIZE, size, PAGINATION_SIZE_BETWEEN, null);
        validateCustom(p -> (long) p * size <= MAX_PAGINATION_OFFSET, page, PAGINATION_MAX_OFFSET, null);

        validateCustom(
                field -> field.equalsIgnoreCase("city")
                        || field.equalsIgnoreCase("department"),
                sortBy,
                LOCATION_SORT_FIELD_INVALID,
                null
        );

        validateCustom(
                direction -> direction.equalsIgnoreCase("ASC")
                        || direction.equalsIgnoreCase("DESC"),
                sortDirection,
                LOCATION_SORT_DIRECTION_INVALID,
                null
        );

        return locationPersistencePort.searchLocations(normalizedText, page, size, sortBy, sortDirection);
    }

    @Override
    public Optional<LocationModel> getLocationById(Long id) {
        return locationPersistencePort.getLocationById(id);
    }

    private String normalizeText(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        return Normalizer.normalize(text.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}", "");
    }
}