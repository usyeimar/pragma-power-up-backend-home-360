package com.pragma.user360.application.services;

import com.pragma.user360.application.dto.request.RegisterUserRequest;
import com.pragma.user360.application.dto.request.filters.UserFilterRequest;
import com.pragma.user360.application.dto.response.PaginatedResponse;
import com.pragma.user360.application.dto.response.UserResponse;
import com.pragma.user360.domain.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserResponse getUserById(Long id);

    Optional<UserModel> getUserByEmail(String email);

    UserResponse saveUser(RegisterUserRequest request);

    UserResponse updateUser(Long id, RegisterUserRequest request);

    void deleteUser(Long id);

    PaginatedResponse<UserResponse> getAllUsers(UserFilterRequest filter);
}