package com.pragma.home360.home.application.services.impl;

import com.pragma.home360.home.application.dto.response.PaginatedResponse;
import com.pragma.home360.home.application.dto.request.filters.FilterRequest;
import com.pragma.home360.home.application.dto.request.SaveDepartmentRequest;
import com.pragma.home360.home.application.dto.response.DepartmentResponse;
import com.pragma.home360.home.application.mappers.DepartmentDtoMapper;
import com.pragma.home360.home.application.services.DepartmentService;
import com.pragma.home360.home.domain.model.DepartmentModel;
import com.pragma.home360.home.domain.ports.in.DepartmentServicePort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentServicePort departmentServicePort;
    private final DepartmentDtoMapper departmentDtoMapper;

    public DepartmentServiceImpl(DepartmentServicePort departmentServicePort, DepartmentDtoMapper departmentDtoMapper) {
        this.departmentServicePort = departmentServicePort;
        this.departmentDtoMapper = departmentDtoMapper;
    }

    @Override
    public DepartmentResponse   saveDepartment(SaveDepartmentRequest request) {
        DepartmentModel departmentModel = departmentDtoMapper.toModel(request);
        DepartmentModel savedDepartment = departmentServicePort.saveDepartment(departmentModel);
        return departmentDtoMapper.toResponse(savedDepartment);
    }

    @Override
    public PaginatedResponse<DepartmentResponse> getAllDepartments(FilterRequest paginationRequest) {
        int page = paginationRequest.page();
        int size = paginationRequest.size();

        List<DepartmentModel> departments = departmentServicePort.getAllDepartments(page, size);
        long totalCount = departmentServicePort.getDepartmentCount();

        List<DepartmentResponse> departmentResponses = departmentDtoMapper.toResponseList(departments);

        int totalPages = size > 0 ? (int) Math.ceil((double) totalCount / size) : 0;

        return new PaginatedResponse<>(departmentResponses,  page, size, totalPages, totalCount);
    }

    @Override
    public DepartmentResponse getDepartmentById(Long id) {
        DepartmentModel departmentModel = departmentServicePort.getDepartmentById(id)
                .orElseThrow(() -> new RuntimeException("Departamento no encontrado con ID: " + id));



        return departmentDtoMapper.toResponse(departmentModel);
    }
}