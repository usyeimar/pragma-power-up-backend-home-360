package com.pragma.home360.home.domain.usecases;

import com.pragma.home360.home.domain.exceptions.AlreadyExistsException;
import com.pragma.home360.home.domain.exceptions.ModelNotFoundException;
import com.pragma.home360.home.domain.model.FilterModel;
import com.pragma.home360.home.domain.model.NeighborhoodModel;
import com.pragma.home360.home.domain.ports.in.NeighborhoodServicePort;
import com.pragma.home360.home.domain.ports.out.CityPersistencePort;
import com.pragma.home360.home.domain.ports.out.NeighborhoodPersistencePort;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;

import java.util.Optional;

import static com.pragma.home360.home.domain.utils.constants.DomainConstants.*;
import static com.pragma.home360.home.domain.utils.constants.Validator.*;

public class NeighborhoodUseCase implements NeighborhoodServicePort {

    private final NeighborhoodPersistencePort neighborhoodPersistencePort;
    private final CityPersistencePort cityPersistencePort;

    public NeighborhoodUseCase(NeighborhoodPersistencePort neighborhoodPersistencePort, CityPersistencePort cityPersistencePort) {
        this.neighborhoodPersistencePort = neighborhoodPersistencePort;
        this.cityPersistencePort = cityPersistencePort;
    }

    @Override
    public NeighborhoodModel saveNeighborhood(NeighborhoodModel neighborhood) {
        validateNotEmpty(neighborhood.getName(), NEIGHBORHOOD_NAME_CANNOT_BE_EMPTY);
        validateNotEmpty(neighborhood.getDescription(), NEIGHBORHOOD_DESCRIPTION_CANNOT_BE_EMPTY);

        validateMaxLength(neighborhood.getName(), NEIGHBORHOOD_NAME_MAX_LENGTH, NEIGHBORHOOD_NAME_MAX_LENGTH_EXCEEDED);
        validateMaxLength(neighborhood.getDescription(), NEIGHBORHOOD_DESCRIPTION_MAX_LENGTH, NEIGHBORHOOD_DESCRIPTION_MAX_LENGTH_EXCEEDED);

        validateCustom(
                name -> !neighborhoodPersistencePort.existsNeighborhoodByName(name.trim()),
                neighborhood.getName(),
                String.format(NEIGHBORHOOD_NAME_ALREADY_EXISTS, neighborhood.getName()),
                AlreadyExistsException.class.getName()
        );


        validateCustom(
                cityId -> cityPersistencePort.getCityById(cityId).isPresent(),
                neighborhood.getCity().getId(),
                String.format(CITY_NOT_FOUND, neighborhood.getCity().getId()),
                ModelNotFoundException.class.getName()
        );

        return neighborhoodPersistencePort.saveNeighborhood(neighborhood);
    }

    @Override
    public Optional<NeighborhoodModel> getNeighborhoodById(Long id) {
        return neighborhoodPersistencePort.getNeighborhoodById(id);
    }

    @Override
    public PagedResult<NeighborhoodModel> getAllNeighborhoods(FilterModel filter) {
        validateFilters(filter);

        return neighborhoodPersistencePort.getAllNeighborhoods(filter);
    }


    @Override
    public PagedResult<NeighborhoodModel> getNeighborhoodsByCity(Long cityId, FilterModel filter) {
        validateCustom(
                id -> cityPersistencePort.getCityById(id).isPresent(),
                cityId,
                String.format(CITY_NOT_FOUND, cityId),
                ModelNotFoundException.class.getName()
        );

        validateFilters(filter);

        return neighborhoodPersistencePort.getNeighborhoodsByCity(cityId, filter);
    }

    @Override
    public boolean existsNeighborhoodByName(String name) {
        return neighborhoodPersistencePort.existsNeighborhoodByName(name);
    }


    private void validateFilters(FilterModel filter) {
        validateCustom(p -> p >= 0, filter.page(), PAGINATION_PAGE_NEGATIVE, null);
        validateCustom(s -> s >= 1 && s <= MAX_PAGE_SIZE, filter.size(), PAGINATION_SIZE_BETWEEN, null);
        validateCustom(p -> (long) p * filter.size() <= MAX_PAGINATION_OFFSET, filter.page(), PAGINATION_MAX_OFFSET, null);
    }
}