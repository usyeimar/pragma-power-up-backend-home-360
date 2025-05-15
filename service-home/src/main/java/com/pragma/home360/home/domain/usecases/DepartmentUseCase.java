package com.pragma.home360.home.domain.usecases;

import com.pragma.home360.home.domain.exceptions.AlreadyExistsException;
import com.pragma.home360.home.domain.model.DepartmentModel;
import com.pragma.home360.home.domain.ports.in.DepartmentServicePort;
import com.pragma.home360.home.domain.ports.out.DepartmentPersistencePort;

import java.util.List;
import java.util.Optional;

import static com.pragma.home360.home.domain.utils.constants.Validator.validateMaxLength;
import static com.pragma.home360.home.domain.utils.constants.Validator.validateCustom;
import static com.pragma.home360.home.domain.utils.constants.Validator.validateNotEmpty;
import static com.pragma.home360.home.domain.utils.constants.DomainConstants.*;

public class DepartmentUseCase implements DepartmentServicePort {

    private final DepartmentPersistencePort departmentPersistencePort;

    public DepartmentUseCase(DepartmentPersistencePort departmentPersistencePort) {
        this.departmentPersistencePort = departmentPersistencePort;
    }

    @Override
    public DepartmentModel saveDepartment(DepartmentModel department) {
        validateNotEmpty(department.getName(), DEPARTMENT_NAME_CANNOT_BE_EMPTY);
        validateNotEmpty(department.getDescription(), DEPARTMENT_DESCRIPTION_CANNOT_BE_EMPTY);
        validateMaxLength(department.getName(), DEPARTMENT_NAME_MAX_LENGTH, DEPARTMENT_NAME_MAX_LENGTH_EXCEEDED);
        validateMaxLength(department.getDescription(), DEPARTMENT_DESCRIPTION_MAX_LENGTH, DEPARTMENT_DESCRIPTION_MAX_LENGTH_EXCEEDED);
        validateCustom(
                name -> !departmentPersistencePort.existsDepartmentByName(name.trim()),
                department.getName(),
                String.format(DEPARTMENT_NAME_ALREADY_EXISTS, department.getName()),
                AlreadyExistsException.class.getName()
        );

        return departmentPersistencePort.saveDepartment(department);
    }

    @Override
    public Optional<DepartmentModel> getDepartmentById(Long id) {
        return departmentPersistencePort.getDepartmentById(id);
    }

    @Override
    public List<DepartmentModel> getAllDepartments(int page, int size) {
        validateFilters(page, size);
        return departmentPersistencePort.getAllDepartments(page, size);
    }


    @Override
    public long getDepartmentCount() {
        return departmentPersistencePort.getDepartmentCount();
    }

    @Override
    public boolean existsDepartmentByName(String name) {
        return departmentPersistencePort.existsDepartmentByName(name);
    }

    static void validateFilters(int page, int size) {
        validateCustom(p -> p >= INITIAL_PAGE, page, PAGINATION_PAGE_NEGATIVE, null);
        validateCustom(s -> s >= MIN_PAGE_SIZE && s <= MAX_PAGE_SIZE, size, PAGINATION_SIZE_BETWEEN, null);
        validateCustom(p -> (long) p * size <= MAX_PAGINATION_OFFSET, page, PAGINATION_MAX_OFFSET, null);
    }
}