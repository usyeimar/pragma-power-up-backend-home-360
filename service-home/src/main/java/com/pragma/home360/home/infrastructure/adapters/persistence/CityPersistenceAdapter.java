package com.pragma.home360.home.infrastructure.adapters.persistence;

import com.pragma.home360.home.domain.model.CityModel;
import com.pragma.home360.home.domain.model.FilterModel;
import com.pragma.home360.home.domain.ports.out.CityPersistencePort;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;
import com.pragma.home360.home.domain.utils.pagination.PaginationUtils;
import com.pragma.home360.home.infrastructure.entities.CityEntity;
import com.pragma.home360.home.infrastructure.mappers.CityEntityMapper;
import com.pragma.home360.home.infrastructure.repositories.mysql.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CityPersistenceAdapter implements CityPersistencePort {

    private final CityRepository cityRepository;
    private final CityEntityMapper cityEntityMapper;

    @Override
    public CityModel saveCity(CityModel cityModel) {
        CityEntity cityEntity = cityEntityMapper.toEntity(cityModel);
        CityEntity savedCity = cityRepository.save(cityEntity);
        return cityEntityMapper.toModel(savedCity);
    }

    @Override
    public Optional<CityModel> getCityById(Long id) {
        return cityRepository.findById(id)
                .map(cityEntityMapper::toModel);
    }

    @Override
    public Optional<CityModel> getCityByName(String name) {
        return cityRepository.findByName(name)
                .map(cityEntityMapper::toModel);
    }

    @Override
    public PagedResult<CityModel> getAllCities(int page, int size) {
        var pageable = PaginationUtils.createPageable(page, size, "id", null);

        return PaginationUtils.toPagedResult(cityRepository.findAll(pageable).map(cityEntityMapper::toModel));
    }

    @Override
    public long getCityCount() {
        return cityRepository.count();
    }

    @Override
    public boolean existsCityByName(String name) {
        return cityRepository.existsByName(name);
    }

    @Override
    public PagedResult<CityModel> getAllCities(FilterModel filter) {
        var pageable = PaginationUtils.createPageable(
                filter.page(),
                filter.size(),
                filter.sortField(),
                filter.direction()
        );

        var page = cityRepository.findAll(pageable);
        return PaginationUtils.toPagedResult(page.map(cityEntityMapper::toModel));
    }
}