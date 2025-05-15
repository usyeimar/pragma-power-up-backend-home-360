package com.pragma.home360.home.infrastructure.mappers;

import com.pragma.home360.home.domain.model.DepartmentModel;
import com.pragma.home360.home.infrastructure.entities.DepartmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DepartmentEntityMapper {

    DepartmentModel toModel(DepartmentEntity entity);

    DepartmentEntity toEntity(DepartmentModel model);
}
