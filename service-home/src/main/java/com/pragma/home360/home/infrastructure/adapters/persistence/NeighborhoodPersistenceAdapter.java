package com.pragma.home360.home.infrastructure.adapters.persistence;

import com.pragma.home360.home.domain.model.FilterModel;
import com.pragma.home360.home.domain.model.NeighborhoodModel;
import com.pragma.home360.home.domain.ports.out.NeighborhoodPersistencePort;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;
import com.pragma.home360.home.domain.utils.pagination.PaginationUtils;
import com.pragma.home360.home.infrastructure.entities.NeighborHoodEntity;
import com.pragma.home360.home.infrastructure.mappers.NeighborhoodEntityMapper;
import com.pragma.home360.home.infrastructure.repositories.mysql.NeighborhoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NeighborhoodPersistenceAdapter implements NeighborhoodPersistencePort {

    private final NeighborhoodRepository neighborhoodRepository;
    private final NeighborhoodEntityMapper neighborhoodEntityMapper;

    @Override
    public NeighborhoodModel saveNeighborhood(NeighborhoodModel neighborhoodModel) {
        NeighborHoodEntity neighborhoodEntity = neighborhoodEntityMapper.toEntity(neighborhoodModel);
        NeighborHoodEntity savedNeighborhood = neighborhoodRepository.save(neighborhoodEntity);
        return neighborhoodEntityMapper.toModel(savedNeighborhood);
    }

    @Override
    public Optional<NeighborhoodModel> getNeighborhoodById(Long id) {
        return neighborhoodRepository.findById(id)
                .map(neighborhoodEntityMapper::toModel);
    }

    @Override
    public Optional<NeighborhoodModel> getNeighborhoodByNameAndCityId(String name, Long cityId) {
        return neighborhoodRepository.findByNameAndCityId(name, cityId)
                .map(neighborhoodEntityMapper::toModel);
    }

    @Override
    public boolean existsNeighborhoodByName(String name) {
        return neighborhoodRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public boolean existsNeighborhoodByNameAndCityId(String name, Long cityId) {
        return neighborhoodRepository.existsByNameIgnoreCaseAndCityId(name, cityId);
    }

    @Override
    public PagedResult<NeighborhoodModel> getAllNeighborhoods(FilterModel filter) {
        var pageable = PaginationUtils.createPageable(
            filter.page(),
            filter.size(),
            filter.sortField(),
            filter.direction()
        );

        var page = neighborhoodRepository.findAll(pageable);
        return PaginationUtils.toPagedResult(page.map(neighborhoodEntityMapper::toModel));
    }

    @Override
    public PagedResult<NeighborhoodModel> getNeighborhoodsByCity(Long cityId, FilterModel filter) {
        var pageable = PaginationUtils.createPageable(
            filter.page(),
            filter.size(),
            filter.sortField(),
            filter.direction()
        );

        var page = neighborhoodRepository.findByCityId(cityId, pageable);
        return PaginationUtils.toPagedResult(page.map(neighborhoodEntityMapper::toModel));
    }
}