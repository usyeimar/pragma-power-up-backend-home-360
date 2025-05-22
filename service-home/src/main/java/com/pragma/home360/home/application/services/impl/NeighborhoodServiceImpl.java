package com.pragma.home360.home.application.services.impl;

import com.pragma.home360.home.application.dto.response.PaginatedResponse;
import com.pragma.home360.home.application.dto.request.SaveNeighborhoodRequest;
import com.pragma.home360.home.application.dto.request.filters.FilterRequest;
import com.pragma.home360.home.application.dto.response.NeighborhoodResponse;
import com.pragma.home360.home.application.mappers.NeighborhoodDtoMapper;
import com.pragma.home360.home.application.services.NeighborhoodService;
import com.pragma.home360.home.domain.exceptions.ModelNotFoundException;
import com.pragma.home360.home.domain.model.CityModel;
import com.pragma.home360.home.domain.model.FilterModel;
import com.pragma.home360.home.domain.model.NeighborhoodModel;
import com.pragma.home360.home.domain.ports.in.CityServicePort;
import com.pragma.home360.home.domain.ports.in.NeighborhoodServicePort;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.pragma.home360.home.domain.utils.constants.DomainConstants.NEIGHBORHOOD_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class NeighborhoodServiceImpl implements NeighborhoodService {

    private final NeighborhoodServicePort neighborhoodServicePort;
    private final CityServicePort cityServicePort;
    private final NeighborhoodDtoMapper neighborhoodDtoMapper;

    @Override
    public NeighborhoodResponse saveNeighborhood(SaveNeighborhoodRequest request) {
        NeighborhoodModel neighborhoodModel = neighborhoodDtoMapper.toModel(request);
        NeighborhoodModel savedNeighborhood = neighborhoodServicePort.saveNeighborhood(neighborhoodModel);

        NeighborhoodResponse baseResponse = neighborhoodDtoMapper.toResponse(savedNeighborhood);

        return new NeighborhoodResponse(
                baseResponse.id(),
                baseResponse.name(),
                baseResponse.description(),
                baseResponse.city()
        );
    }

    @Override
    public NeighborhoodResponse getNeighborhoodById(Long id) {
        NeighborhoodModel neighborhoodModel = neighborhoodServicePort.getNeighborhoodById(id)
                .orElseThrow(() -> new ModelNotFoundException(String.format(NEIGHBORHOOD_NOT_FOUND, id)));

        NeighborhoodResponse baseResponse = neighborhoodDtoMapper.toResponse(neighborhoodModel);

        cityServicePort.getCityById(baseResponse.city().id())
                .orElseThrow(() -> new ModelNotFoundException(String.format(NEIGHBORHOOD_NOT_FOUND, id)));

        return new NeighborhoodResponse(
                baseResponse.id(),
                baseResponse.name(),
                baseResponse.description(),
                baseResponse.city()
        );
    }


    @Override
    public PaginatedResponse<NeighborhoodResponse> getAllNeighborhoods(FilterRequest filter) {
        PagedResult<NeighborhoodModel> neighborhoodPage = neighborhoodServicePort.getAllNeighborhoods(
                new FilterModel(
                        filter.page(),
                        filter.size(),
                        filter.sortField(),
                        filter.direction(),
                        filter.searchTerm(),
                        filter.active(),
                        filter.startDate(),
                        filter.endDate()
                ));

        List<NeighborhoodResponse> neighborhoods = neighborhoodDtoMapper.fromModelListToResponseList(neighborhoodPage.content());

        return new PaginatedResponse<>(
                neighborhoods,
                neighborhoodPage.page(),
                neighborhoodPage.size(),
                neighborhoodPage.totalPages(),
                neighborhoodPage.totalElements()
        );
    }

    @Override
    public PaginatedResponse<NeighborhoodResponse> getNeighborhoodsByCity(Long cityId, FilterRequest filter) {
        PagedResult<NeighborhoodModel> neighborhoodPage = neighborhoodServicePort.getNeighborhoodsByCity(cityId, new FilterModel(
                filter.page(),
                filter.size(),
                filter.sortField(),
                filter.direction(),
                filter.searchTerm(),
                filter.active(),
                filter.startDate(),
                filter.endDate()
        ));

        List<NeighborhoodResponse> neighborhoods = neighborhoodDtoMapper.fromModelListToResponseList(neighborhoodPage.content());

        return new PaginatedResponse<>(
                neighborhoods,
                neighborhoodPage.page(),
                neighborhoodPage.size(),
                neighborhoodPage.totalPages(),
                neighborhoodPage.totalElements()
        );
    }


}