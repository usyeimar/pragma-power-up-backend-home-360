package com.pragma.home360.home.infrastructure.repositories.mysql;

import com.pragma.home360.home.infrastructure.entities.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> findByName(String name);

    Page<CategoryEntity> findAll(Pageable pageable);

    @Query("SELECT c FROM CategoryEntity c WHERE (:name IS NULL OR c.name LIKE %:name%)")
    Page<CategoryEntity> findByNameContaining(@Param("name") String name, Pageable pageable);
}
