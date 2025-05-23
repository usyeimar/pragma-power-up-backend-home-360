package com.pragma.user360.application.services.impl;

import com.pragma.user360.application.dto.request.SignUpRequest;
import com.pragma.user360.application.dto.response.LoginResponse;
import com.pragma.user360.application.dto.response.UserResponse;
import com.pragma.user360.application.services.AuthService;
import com.pragma.user360.domain.model.UserModel;
import com.pragma.user360.domain.ports.in.UserServicePort;
import com.pragma.user360.infrastructure.adapters.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserServicePort userServicePort;
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Value("${app.jwt.prefix:Bearer }")
    private String tokenPrefix;

    @Value("${app.jwt.expiration-ms}")
    private Long tokenExpirationMs;

    @Override
    public LoginResponse signIn(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                email,
                password
        );
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        JwtTokenProvider.TokenDetails tokenDetails = tokenProvider.generateTokenDetails(authentication);
        String jwt = tokenDetails.token();
        LocalDateTime issuedAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(tokenDetails.issuedAtDate().getTime()), ZoneOffset.UTC);

        log.info("User {} authenticated successfully. Token issued at: {}", email, issuedAt);

        UserModel userModel = userServicePort.getUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el correo: " + email));

        UserResponse userProfile = new UserResponse(
                userModel.getId(),
                userModel.getFirstName(),
                userModel.getLastName(),
                userModel.getEmail(),
                userModel.getRole(),
                userModel.getActive(),
                userModel.getCreatedAt() != null ? userModel.getCreatedAt().atStartOfDay() : null
        );

        return new LoginResponse(
                tokenPrefix.trim(),
                jwt,
                tokenExpirationMs,
                issuedAt,
                userProfile
        );
    }

    @Override
    public void signUp(SignUpRequest request) {
        log.info("Sign-up request received for email: {}. (Actual implementation pending)", request.email());
    }
}
