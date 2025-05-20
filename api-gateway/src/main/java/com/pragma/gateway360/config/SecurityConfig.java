package com.pragma.gateway360.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.gateway360.infrastructure.exceptionshandler.ExceptionResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    private static final String JWT_ALGORITHM = "HmacSHA256";

    @Value("${app.jwt.secret}")
    private String jwtSecretString;

    private final ObjectMapper objectMapper;

    public SecurityConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public SecurityFilterChain customApiGatewaySecurityFilterChain(HttpSecurity http, ClaimsToHeadersFilter claimsToHeadersFilter) throws Exception {
        log.info("Configuring SecurityFilterChain for API Gateway (customApiGatewaySecurityFilterChain)...");
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/webjars/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder()))
                        .authenticationEntryPoint(customAuthenticationEntryPoint())
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable);

        http.addFilterAfter(claimsToHeadersFilter, BearerTokenAuthenticationFilter.class);
        log.info("SecurityFilterChain configured successfully.");
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        if (!StringUtils.hasText(jwtSecretString)) {
            log.error("CRITICAL: API Gateway - JWT Secret ('app.jwt.secret') is MISSING or EMPTY. Cannot create JwtDecoder. Application will likely fail to authenticate JWTs.");
            throw new IllegalStateException("JWT Secret ('app.jwt.secret') is missing or empty. This is required for API Gateway JwtDecoder.");
        }
        log.info("API Gateway JwtDecoder: Initializing with configured JWT secret for algorithm {}.", JWT_ALGORITHM);

        byte[] keyBytes = jwtSecretString.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = new SecretKeySpec(keyBytes, JWT_ALGORITHM);

        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(secretKey).build();
        log.info("API Gateway JwtDecoder configured successfully for {}.", JWT_ALGORITHM);
        return decoder;
    }

    @Bean
    public SecretKey sharedSecretKey() {
        if (!StringUtils.hasText(jwtSecretString)) {
            log.error("CRITICAL: API Gateway - JWT Secret ('app.jwt.secret') is MISSING or EMPTY. Cannot create SharedSecretKey bean.");
        }
        byte[] keyBytes = jwtSecretString.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = new SecretKeySpec(keyBytes, JWT_ALGORITHM);
        log.info("Shared SecretKey Bean (API Gateway) created for {} algorithm.", JWT_ALGORITHM);
        return secretKey;
    }

    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            log.warn("API Gateway Authentication Entry Point: Authentication failed. Path: {}, Error: {}", request.getRequestURI(), authException.getMessage());
            Throwable cause = authException.getCause();
            if (cause != null) {
                log.warn("Underlying cause: Type [{}], Message [{}]", cause.getClass().getName(), cause.getMessage());
            }

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            String errorTitle = "Authentication Error";
            String errorDetails = "Access denied. Please provide a valid authentication token.";

            if (authException instanceof InvalidBearerTokenException) {
                errorTitle = "Invalid Bearer Token";
                errorDetails = "The provided bearer token is malformed or invalid. " + authException.getMessage();
            } else if (authException instanceof OAuth2AuthenticationException oauthEx) {
                OAuth2Error oauth2Error = oauthEx.getError();
                errorTitle = "JWT Authentication Failed";
                if (oauth2Error != null) {
                    errorDetails = StringUtils.hasText(oauth2Error.getDescription()) ? oauth2Error.getDescription() : oauthEx.getMessage();
                    log.warn("OAuth2AuthenticationException: Error Code [{}], Description [{}], URI [{}]",
                            oauth2Error.getErrorCode(), oauth2Error.getDescription(), oauth2Error.getUri());
                } else {
                    errorDetails = oauthEx.getMessage();
                }

                if (cause instanceof JwtValidationException jwtValEx) {
                    errorTitle = "JWT Validation Failed";
                    String validationErrors = jwtValEx.getErrors().stream()
                            .map(e -> e.getDescription() + " (Error Code: " + e.getErrorCode() + ")")
                            .collect(Collectors.joining("; "));
                    errorDetails = "The JWT is invalid: " + validationErrors;
                    log.warn("JWT Validation Exception Details: {}", validationErrors);
                } else if (cause instanceof IllegalArgumentException && cause.getMessage() != null && cause.getMessage().contains("JWT String argument cannot be null or empty")) {
                    errorTitle = "Missing JWT";
                    errorDetails = "The JWT is missing. Please include a valid token in the Authorization header.";
                } else if (cause != null) {
                    errorDetails = "Authentication failed due to: " + cause.getClass().getSimpleName() + " - " + cause.getMessage();
                }
            } else {
                errorDetails = authException.getMessage();
            }


            ExceptionResponse errorResponse = new ExceptionResponse(
                    false,
                    errorTitle,
                    LocalDateTime.now(),
                    errorDetails
            );

            try {
                response.getOutputStream().println(objectMapper.writeValueAsString(errorResponse));
            } catch (IOException e) {
                log.error("Error writing authentication error response to output stream", e);
            }
        };
    }
}
