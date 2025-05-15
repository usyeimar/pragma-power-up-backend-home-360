package com.pragma.user360.domain.ports.in;

import com.pragma.user360.application.dto.request.filters.UserFilterRequest;
import com.pragma.user360.domain.model.UserFilterModel;
import com.pragma.user360.domain.model.UserModel;
import com.pragma.user360.domain.utils.pagination.PagedResult;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserServicePort {
    UserModel registerUser(UserModel userModel);


    PagedResult<UserModel> getAllUsers(UserFilterModel filter);

    Optional<UserModel> getUserById(Long id);

    Optional<UserModel> getUserByEmail(String email);

    Optional<UserModel> getUserByDocumentId(String documentId);

    boolean existsByEmail(String email);

    boolean existsByDocumentId(String documentId);

    boolean deleteUser(Long id);
}