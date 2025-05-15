package com.pragma.home360.home.infrastructure.adapters.persistence;

import com.pragma.home360.home.domain.model.DepartmentModel;
import com.pragma.home360.home.domain.ports.out.DepartmentPersistencePort;
import com.pragma.home360.home.infrastructure.entities.DepartmentEntity;
import com.pragma.home360.home.infrastructure.mappers.DepartmentEntityMapper;
import com.pragma.home360.home.infrastructure.repositories.mysql.DepartmentRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DepartmentPersistenceAdapter implements DepartmentPersistencePort {

    private final DepartmentRepository departmentRepository;
    private final DepartmentEntityMapper departmentEntityMapper;

    public DepartmentPersistenceAdapter(DepartmentRepository departmentRepository, DepartmentEntityMapper departmentEntityMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentEntityMapper = departmentEntityMapper;
    }

    @Override
    public DepartmentModel saveDepartment(DepartmentModel departmentModel) {
        DepartmentEntity departmentEntity = departmentEntityMapper.toEntity(departmentModel);
        departmentEntity = departmentRepository.save(departmentEntity);
        return departmentEntityMapper.toModel(departmentEntity);
    }

    @Override
    public Optional<DepartmentModel> getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .map(departmentEntityMapper::toModel);
    }

    @Override
    public List<DepartmentModel> getAllDepartments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return departmentRepository.findAll(pageable).getContent()
                .stream()
                .map(departmentEntityMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public long getDepartmentCount() {
        return departmentRepository.count();
    }

    @Override
    public boolean existsDepartmentByName(String name) {
        return departmentRepository.existsByNameIgnoreCase(name);
    }
}