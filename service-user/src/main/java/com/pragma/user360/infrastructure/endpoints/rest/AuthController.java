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
@Tag(name = "Autenticación", description = "Operaciones de autenticación y registro de usuarios")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-in")
    @Operation(summary = "Iniciar sesión de usuario",
            description = "Autentica a un usuario con su correo electrónico y contraseña. Devuelve un token de acceso JWT junto con detalles del token y el perfil del usuario.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Autenticación exitosa",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LoginResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. campos faltantes o formato incorrecto)"),
                    @ApiResponse(responseCode = "401", description = "No autorizado (credenciales incorrectas o usuario inactivo)")
            })
    public ResponseEntity<LoginResponse> signIn(@Valid @RequestBody SignInRequest loginRequest) {
        LoginResponse response = authService.signIn(loginRequest.email(), loginRequest.password());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sign-up")
    @Operation(summary = "Registrar un nuevo usuario (Platzhalter)",
            description = "Endpoint para el registro de nuevos usuarios. Actualmente es un Platzhalter y no implementa la lógica de creación de usuario.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuario creado (funcionalidad no implementada completamente)",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
                    @ApiResponse(responseCode = "409", description = "Conflicto (ej. usuario ya existe)")
            })
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest request) {
        authService.signUp(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}