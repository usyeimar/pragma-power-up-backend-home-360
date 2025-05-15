package com.pragma.home360.home.infrastructure.adapters.persistence;

import com.pragma.home360.home.application.dto.response.PaginatedResponse;
import com.pragma.home360.home.domain.model.LocationModel;
import com.pragma.home360.home.domain.ports.out.LocationPersistencePort;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;
import com.pragma.home360.home.infrastructure.entities.LocationEntity;
import com.pragma.home360.home.infrastructure.mappers.LocationEntityMapper;
import com.pragma.home360.home.infrastructure.repositories.mysql.LocationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class LocationPersistenceAdapter implements LocationPersistencePort {

    private final LocationRepository locationRepository;
    private final LocationEntityMapper locationEntityMapper;


    public LocationPersistenceAdapter(LocationRepository locationRepository, LocationEntityMapper locationEntityMapper) {
        this.locationRepository = locationRepository;
        this.locationEntityMapper = locationEntityMapper;
    }

    @Override
    public LocationModel saveLocation(LocationModel location) {
        LocationEntity entity = locationEntityMapper.toEntity(location);
        entity = locationRepository.save(entity);
        return locationEntityMapper.toModel(entity);
    }

    @Override
    public Optional<LocationModel> getLocationById(Long id) {
        return locationRepository.findById(id)
                .map(locationEntityMapper::toModel);
    }

    @Override
    public PagedResult<LocationModel> searchLocations(String normalizedText, int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        String sortField = determineSortField(sortBy);
        Sort sort = Sort.by(direction, sortField);

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<LocationEntity> locationPage = locationRepository.searchLocations(normalizedText, pageable);
        List<LocationModel> locations = locationEntityMapper.toModelList(locationPage.getContent());

        return new PagedResult<>(
                locations,
                locationPage.getNumber(),
                locationPage.getSize(),
                locationPage.getTotalElements(),
                locationPage.getTotalPages()
        );
    }


    /**
     * Determina el campo de ordenamiento basado en la solicitud del usuario
     *
     * @param sortBy Campo solicitado (city o department)
     * @return Campo real en la consulta SQL
     */
    private String determineSortField(String sortBy) {
        if (sortBy.equalsIgnoreCase("department")) {
            return "neighborhood.city.department.name";
        }
        return "neighborhood.city.name";
    }
}