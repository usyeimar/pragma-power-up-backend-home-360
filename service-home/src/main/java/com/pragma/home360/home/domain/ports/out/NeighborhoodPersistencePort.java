package com.pragma.home360.home.domain.ports.out;

import com.pragma.home360.home.domain.model.FilterModel;
import com.pragma.home360.home.domain.model.NeighborhoodModel;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;

import java.util.Optional;

public interface NeighborhoodPersistencePort {
    NeighborhoodModel saveNeighborhood(NeighborhoodModel neighborhoodModel);
    
    Optional<NeighborhoodModel> getNeighborhoodById(Long id);
    
    Optional<NeighborhoodModel> getNeighborhoodByNameAndCityId(String name, Long cityId);
    
    boolean existsNeighborhoodByName(String name);
    
    boolean existsNeighborhoodByNameAndCityId(String name, Long cityId);
    
    PagedResult<NeighborhoodModel> getAllNeighborhoods(FilterModel filter);
    
    PagedResult<NeighborhoodModel> getNeighborhoodsByCity(Long cityId, FilterModel filter);
}