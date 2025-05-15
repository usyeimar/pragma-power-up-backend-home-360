package com.pragma.user360.infrastructure.endpoints.rest;

import com.pragma.user360.application.dto.request.RegisterUserRequest;
import com.pragma.user360.application.dto.request.filters.UserFilterRequest;
import com.pragma.user360.application.dto.response.PaginatedResponse;
import com.pragma.user360.application.dto.response.UserResponse;
import com.pragma.user360.application.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get all users with pagination")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Users found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))), @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @GetMapping
    public ResponseEntity<PaginatedResponse<UserResponse>> getAllUsers(
            @ParameterObject UserFilterRequest filter
    ) {
        PaginatedResponse<UserResponse> users = userService.getAllUsers(filter);
        return ResponseEntity.ok(users);
    }


    @Operation(summary = "Get user by ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))), @ApiResponse(responseCode = "404", description = "User not found", content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "User created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))), @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)})
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody RegisterUserRequest request) {
        UserResponse savedUser = userService.saveUser(request);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a user")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))), @ApiResponse(responseCode = "404", description = "User not found", content = @Content)})
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody RegisterUserRequest request) {
        UserResponse updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Delete a user")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "User deleted"), @ApiResponse(responseCode = "404", description = "User not found", content = @Content)})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}