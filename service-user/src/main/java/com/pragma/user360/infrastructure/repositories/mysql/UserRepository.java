package com.pragma.user360.infrastructure.repositories.mysql;

import com.pragma.user360.infrastructure.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByDocumentId(String documentId);

    boolean existsByEmail(String email);

    boolean existsByDocumentId(String documentId);
}
