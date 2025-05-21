package com.pragma.home360.home.infrastructure.repositories.mysql;

import com.pragma.home360.home.infrastructure.entities.PropertyImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyImageRepository extends JpaRepository<PropertyImageEntity, Long> {
    List<PropertyImageEntity> findAllByPropertyId(Long propertyId);

    Optional<PropertyImageEntity> findByPropertyIdAndIsMainImageTrue(Long propertyId);

    @Modifying
    @Query("UPDATE PropertyImageEntity pie SET pie.isMainImage = false WHERE pie.property.id = :propertyId AND pie.isMainImage = true")
    void clearMainImageFlagForProperty(@Param("propertyId") Long propertyId);
}