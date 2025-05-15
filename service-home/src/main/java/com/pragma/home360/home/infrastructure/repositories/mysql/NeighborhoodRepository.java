package com.pragma.home360.home.infrastructure.repositories.mysql;

import com.pragma.home360.home.infrastructure.entities.NeighborHoodEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NeighborhoodRepository extends JpaRepository<NeighborHoodEntity, Long> {

    Page<NeighborHoodEntity> findAll(Pageable pageable);

    Page<NeighborHoodEntity> findByCityId(Long cityId, Pageable pageable);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndCityId(String name, Long cityId);

    Optional<NeighborHoodEntity> findByNameAndCityId(String name, Long id);
}