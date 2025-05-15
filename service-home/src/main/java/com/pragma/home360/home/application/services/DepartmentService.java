package com.pragma.home360.home.application.services;


import com.pragma.home360.home.application.dto.response.PaginatedResponse;
import com.pragma.home360.home.application.dto.request.filters.FilterRequest;
import com.pragma.home360.home.application.dto.request.SaveDepartmentRequest;
import com.pragma.home360.home.application.dto.response.DepartmentResponse;

public interface DepartmentService {

    DepartmentResponse saveDepartment(SaveDepartmentRequest request);

    PaginatedResponse<DepartmentResponse> getAllDepartments(FilterRequest paginationRequest);

    DepartmentResponse getDepartmentById(Long id);
}