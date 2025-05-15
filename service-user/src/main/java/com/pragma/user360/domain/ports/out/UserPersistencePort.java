package com.pragma.user360.domain.ports.out;

import com.pragma.user360.domain.model.UserFilterModel;
import com.pragma.user360.domain.model.UserModel;
import com.pragma.user360.domain.utils.pagination.PagedResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserPersistencePort {
    UserModel saveUser(UserModel userModel);

    Optional<UserModel> getUserById(Long id);

    Optional<UserModel> getUserByEmail(String email);

    Optional<UserModel> getUserByDocumentId(String documentId);

    boolean existsByEmail(String email);

    boolean existsByDocumentId(String documentId);

    PagedResult<UserModel> getAllUsers(UserFilterModel filter);

    void deleteUser(Long id);

}