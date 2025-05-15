package com.pragma.home360.home.application.mappers;

import com.pragma.home360.home.application.dto.request.SaveDepartmentRequest;
import com.pragma.home360.home.application.dto.response.DepartmentResponse;
import com.pragma.home360.home.domain.model.DepartmentModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DepartmentDtoMapper {

    DepartmentResponse toResponse(DepartmentModel departmentModel);

    List<DepartmentResponse> toResponseList(List<DepartmentModel> departmentModelList);

    DepartmentModel toModel(SaveDepartmentRequest saveDepartmentRequest);
}