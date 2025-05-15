package com.pragma.home360.home.domain.ports.in;

import com.pragma.home360.home.domain.model.DepartmentModel;

import java.util.List;
import java.util.Optional;

public interface DepartmentServicePort {

    DepartmentModel saveDepartment(DepartmentModel department);

    List<DepartmentModel> getAllDepartments(int page, int size);

    Optional<DepartmentModel> getDepartmentById(Long id);

    long getDepartmentCount();

    boolean existsDepartmentByName(String name);
}