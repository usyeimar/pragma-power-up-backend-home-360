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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserServicePort userServicePort;
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Override
    public LoginResponse signIn(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                email,
                password
        );
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        log.info("User {} authenticated successfully", email);
        UserModel userModel = userServicePort.getUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el correo: " + email));

        log.debug("User found: {}", userModel.getEmail());

        List<GrantedAuthority> authorities;
        if (userModel.getRole() != null && !userModel.getRole().isBlank()) {
            // Prefixing with "ROLE_" is a common convention in Spring Security
            authorities = List.of(new SimpleGrantedAuthority("ROLE_" + userModel.getRole()));
            log.debug("User authorities: {}", authorities);
        } else {
            log.warn("User {} has no role assigned.", email);
            authorities = Collections.emptyList();
        }

        return new LoginResponse(
                "Sesion iniciada correctamente, recuerde que el token tiene una validez de 1 hora",
                jwt,
                new UserResponse(
                        userModel.getId(),
                        userModel.getFirstName(),
                        userModel.getLastName(),
                        userModel.getEmail(),
                        userModel.getRole(),
                        userModel.getActive(),
                        userModel.getCreatedAt().atTime(0, 0)
                )
        );

    }

    @Override
    public void signUp(SignUpRequest request) {

    }


}
