package com.pragma.user360.infrastructure.endpoints.rest;


import com.pragma.user360.application.dto.request.SignInRequest;
import com.pragma.user360.application.dto.request.SignUpRequest;
import com.pragma.user360.application.dto.response.LoginResponse;
import com.pragma.user360.application.dto.response.UserResponse;
import com.pragma.user360.application.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticación", description = "Operaciones de autenticación de usuarios")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-in")
    @Operation(summary = "Iniciar sesión", description = "Autentica a un usuario con su correo y contraseña y devuelve un token JWT.", responses = {@ApiResponse(responseCode = "200", description = "Autenticación exitosa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))), @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"), @ApiResponse(responseCode = "401", description = "Credenciales inválidas")})
    public ResponseEntity<LoginResponse> signIn(@Valid @RequestBody SignInRequest loginRequest) {
        LoginResponse response = authService.signIn(loginRequest.email(), loginRequest.password());
        return ResponseEntity.ok(response);
    }


    @PostMapping("/sign-up")
    @Operation(summary = "Por defecto el usurio creado es comprador", description = "Crea un nuevo usuario con rol de vendedor en el sistema.", responses = {@ApiResponse(responseCode = "201", description = "Usuario creado con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))), @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"), @ApiResponse(responseCode = "409", description = "Ya existe un usuario con el mismo email o documento")})
    public ResponseEntity signUp(@RequestBody SignUpRequest request) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
