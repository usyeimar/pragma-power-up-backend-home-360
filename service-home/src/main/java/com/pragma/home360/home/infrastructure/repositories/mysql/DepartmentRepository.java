package com.pragma.home360.home.infrastructure.repositories.mysql;


import com.pragma.home360.home.infrastructure.entities.DepartmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {
    boolean existsByNameIgnoreCase(String name);

    Page<DepartmentEntity> findAll(Pageable pageable);

    Optional<DepartmentEntity> findByNameIgnoreCase(String departmentName);
}