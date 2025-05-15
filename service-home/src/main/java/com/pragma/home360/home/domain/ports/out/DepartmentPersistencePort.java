package com.pragma.home360.home.domain.ports.out;

import com.pragma.home360.home.domain.model.DepartmentModel;

import java.util.List;
import java.util.Optional;

public interface DepartmentPersistencePort {
    DepartmentModel saveDepartment(DepartmentModel department);

    Optional<DepartmentModel> getDepartmentById(Long id);

    List<DepartmentModel> getAllDepartments(int page, int size);

    long getDepartmentCount();

    boolean existsDepartmentByName(String name);
}