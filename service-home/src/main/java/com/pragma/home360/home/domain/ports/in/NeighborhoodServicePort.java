package com.pragma.home360.home.domain.ports.in;

import com.pragma.home360.home.domain.model.FilterModel;
import com.pragma.home360.home.domain.model.NeighborhoodModel;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;

import java.util.Optional;

public interface NeighborhoodServicePort {

    NeighborhoodModel saveNeighborhood(NeighborhoodModel neighborhood);

    NeighborhoodModel getNeighborhoodById(Long id);


    PagedResult<NeighborhoodModel> getAllNeighborhoods(FilterModel filter);

    PagedResult<NeighborhoodModel> getNeighborhoodsByCity(Long cityId, FilterModel filter);

    boolean existsNeighborhoodByName(String name);
}