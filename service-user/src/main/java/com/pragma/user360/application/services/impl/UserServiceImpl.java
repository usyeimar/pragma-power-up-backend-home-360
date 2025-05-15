package com.pragma.user360.application.services.impl;


import com.pragma.user360.application.dto.request.RegisterUserRequest;
import com.pragma.user360.application.dto.request.filters.UserFilterRequest;
import com.pragma.user360.application.dto.response.PaginatedResponse;
import com.pragma.user360.application.dto.response.UserResponse;
import com.pragma.user360.application.mappers.UserDtoMapper;
import com.pragma.user360.application.services.UserService;
import com.pragma.user360.domain.exceptions.ModelNotFoundException;
import com.pragma.user360.domain.model.UserFilterModel;
import com.pragma.user360.domain.model.UserModel;
import com.pragma.user360.domain.ports.in.UserServicePort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.pragma.user360.domain.utils.constants.DomainConstants.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserServicePort userServicePort;
    private final UserDtoMapper userDtoMapper;
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);


    @Override
    public UserResponse getUserById(Long id) {
        UserModel userModel = userServicePort.getUserById(id)
                .orElseThrow(() -> new ModelNotFoundException(
                        String.format(USER_NOT_FOUND, id)
                ));
        return userDtoMapper.modelToResponse(userModel);
    }

    @Override
    public Optional<UserModel> getUserByEmail(String email) {
        UserModel userModel = userServicePort.getUserByEmail(email)
                .orElseThrow(() -> new ModelNotFoundException(
                        String.format(USER_NOT_FOUND, email)
                ));
        log.info("User found: {}", userModel.getEmail());
        log.info("User authorities: {}", userModel.getRole());

        return Optional.of(userModel);
    }

    @Override
    public UserResponse saveUser(RegisterUserRequest request) {
        UserModel userModel = userDtoMapper.requestToModel(request);

        UserModel savedUser = userServicePort.registerUser(userModel);
        log.info("User registered successfully: {}", savedUser);
        return userDtoMapper.modelToResponse(savedUser);
    }

    @Override
    public UserResponse updateUser(Long id, RegisterUserRequest request) {
        return null;
    }

    @Override
    public void deleteUser(Long id) {
        UserModel userModel = userServicePort.getUserById(id)
                .orElseThrow(() -> new ModelNotFoundException(
                        String.format(USER_NOT_FOUND, id)
                ));
        userServicePort.deleteUser(userModel.getId());
        log.info("User deleted successfully: {}", userModel);
    }


    @Override
    public PaginatedResponse<UserResponse> getAllUsers(UserFilterRequest request) {

        var userPage = userServicePort.getAllUsers(
                new UserFilterModel(
                        request.page(),
                        request.size(),
                        request.sortField(),
                        request.direction()
                )
        );

        return new PaginatedResponse<>(
                userPage.content()
                        .stream()
                        .map(userDtoMapper::modelToResponse)
                        .toList(),
                userPage.page(),
                userPage.size(),
                userPage.totalPages(),
                userPage.totalElements()
        );
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserModel> userModelOpt = userServicePort.getUserByEmail(username);

        if (userModelOpt.isEmpty()) {
            log.warn("User not found with email: {}", username);
            throw new UsernameNotFoundException("Usuario no encontrado con el correo: " + username);
        }

        UserModel userModel = userModelOpt.get();
        log.debug("User found: {}", userModel.getEmail());

        List<GrantedAuthority> authorities;
        if (userModel.getRole() != null && !userModel.getRole().isBlank()) {
            // Prefixing with "ROLE_" is a common convention in Spring Security
            authorities = List.of(new SimpleGrantedAuthority("ROLE_" + userModel.getRole()));
            log.debug("User authorities: {}", authorities);
        } else {
            log.warn("User {} has no role assigned.", username);
            authorities = Collections.emptyList();
        }

        return new User(
                userModel.getEmail(),
                userModel.getPassword(),
                userModel.getActive(),
                true,
                true,
                userModel.getActive(),
                authorities
        );
    }

}