package com.pragma.home360.home.infrastructure.repositories.mysql;

import com.pragma.home360.home.infrastructure.entities.PropertyEntity;
import com.pragma.home360.home.infrastructure.entities.enums.PropertyPublicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface PropertyRepository extends JpaRepository<PropertyEntity, Long>, JpaSpecificationExecutor<PropertyEntity> {
    boolean existsByName(String name);

    List<PropertyEntity> findAllByPublicationStatusAndActivePublicationDateLessThanEqual(
            PropertyPublicationStatus publicationStatus,
            LocalDate activePublicationDate
    );

}
