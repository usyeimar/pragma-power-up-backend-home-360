package com.pragma.gateway360.shared.configurations;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.gateway360.infrastructure.exceptionshandler.ExceptionResponse;
import com.pragma.gateway360.shared.constants.GatewayConstants;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    private static final String JWT_ALGORITHM = "HmacSHA512";

    @Value("${app.jwt.secret}")
    private String jwtSecretString;

    private final ObjectMapper objectMapper;
    @Value("${app.allowed-origins}")
    private String allowedOrigins;

    public SecurityConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public SecurityFilterChain customApiGatewaySecurityFilterChain(HttpSecurity http, ClaimsToHeadersFilter claimsToHeadersFilter) throws Exception {
        log.info("Configuring SecurityFilterChain for API Gateway (customApiGatewaySecurityFilterChain)...");
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/actuator/**", // Añadido para permitir acceso a Actuator endpoints
                                "/",
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
                .cors(customizer -> customizer.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable);

        http.addFilterAfter(claimsToHeadersFilter, BearerTokenAuthenticationFilter.class);
        log.info("SecurityFilterChain configured successfully.");
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        if (!StringUtils.hasText(jwtSecretString)) {
            log.error("CRITICAL: API Gateway - JWT Secret ('app.jwt.secret') is MISSING or EMPTY. Cannot create JwtDecoder. Application will likely fail to authenticate JWTs.");
            throw new IllegalStateException(GatewayConstants.JWT_ILLEGAL_STATE_SECRET_MISSING);
        }
        log.info("API Gateway JwtDecoder: Initializing with configured JWT secret for algorithm {}.", JWT_ALGORITHM);
        byte[] keyBytes = jwtSecretString.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = new SecretKeySpec(keyBytes, JWT_ALGORITHM);
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }

    @Bean
    public SecretKey sharedSecretKey() {
        if (!StringUtils.hasText(jwtSecretString)) {
            log.error("CRITICAL: API Gateway - JWT Secret ('app.jwt.secret') is MISSING or EMPTY. Cannot create SharedSecretKey bean.");
            throw new IllegalStateException(GatewayConstants.JWT_ILLEGAL_STATE_SHARED_SECRET_MISSING);
        }
        byte[] keyBytes = jwtSecretString.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = new SecretKeySpec(keyBytes, JWT_ALGORITHM);
        log.info("Shared SecretKey Bean (API Gateway) created for {} algorithm.", JWT_ALGORITHM);
        return secretKey;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        if (StringUtils.hasText(allowedOrigins)) {
            List<String> allowedOriginList = Arrays.stream(allowedOrigins.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
            configuration.setAllowedOrigins(allowedOriginList);
        } else {
            log.warn("No se han configurado orígenes permitidos para CORS. Se permitirán todos los orígenes.");
            configuration.setAllowedOrigins(List.of("*"));
        }

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers", "X-User-Id", "X-User-Roles"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type", "X-User-Id", "X-User-Roles"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        log.info("Fuente de configuracion CORS registrada para todas las rutas ('/**').");
        return source;
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

            String errorCodeValue = GatewayConstants.ERROR_CODE_AUTH_GENERIC;
            String errorTitle = GatewayConstants.ERROR_TITLE_AUTH_GENERIC;
            String errorDetails = GatewayConstants.ERROR_DETAILS_AUTH_GENERIC;

            if (authException instanceof InvalidBearerTokenException) {
                errorCodeValue = GatewayConstants.ERROR_CODE_INVALID_BEARER_TOKEN;
                errorTitle = GatewayConstants.ERROR_TITLE_INVALID_BEARER_TOKEN;
                errorDetails = GatewayConstants.ERROR_DETAILS_INVALID_BEARER_TOKEN_PREFIX + authException.getMessage();
            } else if (authException instanceof OAuth2AuthenticationException oauthEx) {
                errorCodeValue = GatewayConstants.ERROR_CODE_JWT_AUTH_FAILED;
                OAuth2Error oauth2Error = oauthEx.getError();
                errorTitle = GatewayConstants.ERROR_TITLE_JWT_AUTH_FAILED;
                if (oauth2Error != null) {
                    errorDetails = StringUtils.hasText(oauth2Error.getDescription()) ? oauth2Error.getDescription() : oauthEx.getMessage();
                    if (StringUtils.hasText(oauth2Error.getErrorCode())) {
                        errorCodeValue = "JWT_" + oauth2Error.getErrorCode().toUpperCase();
                    }
                } else {
                    errorDetails = oauthEx.getMessage();
                }

                if (cause instanceof JwtValidationException jwtValEx) {
                    errorCodeValue = GatewayConstants.ERROR_CODE_JWT_VALIDATION_FAILED;
                    errorTitle = GatewayConstants.ERROR_TITLE_JWT_VALIDATION_FAILED;
                    String validationErrors = jwtValEx.getErrors().stream()
                            .map(e -> e.getDescription() + " (Código de Error: " + e.getErrorCode() + ")")
                            .collect(Collectors.joining("; "));
                    errorDetails = GatewayConstants.ERROR_DETAILS_JWT_VALIDATION_FAILED_PREFIX + validationErrors;
                } else if (cause instanceof IllegalArgumentException && cause.getMessage() != null && cause.getMessage().contains("JWT String argument cannot be null or empty")) {
                    errorCodeValue = GatewayConstants.ERROR_CODE_JWT_MISSING;
                    errorTitle = GatewayConstants.ERROR_TITLE_JWT_MISSING;
                    errorDetails = GatewayConstants.ERROR_DETAILS_JWT_MISSING;
                } else if (cause != null) {
                    errorDetails = GatewayConstants.ERROR_DETAILS_AUTH_FAILED_DUE_TO_PREFIX + cause.getClass().getSimpleName() + " - " + cause.getMessage();
                }
            } else {
                errorDetails = authException.getMessage();
            }

            ExceptionResponse errorResponse = new ExceptionResponse(
                    false,
                    errorCodeValue,
                    errorTitle,
                    LocalDateTime.now(),
                    errorDetails,
                    null
            );

            try {
                response.getOutputStream().println(objectMapper.writeValueAsString(errorResponse));
            } catch (IOException e) {
                log.error(GatewayConstants.LOG_ERROR_WRITING_AUTH_RESPONSE, e);
            }
        };
    }
}
