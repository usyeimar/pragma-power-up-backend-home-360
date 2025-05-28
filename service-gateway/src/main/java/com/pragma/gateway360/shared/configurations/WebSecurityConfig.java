package com.pragma.gateway360.shared.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.gateway360.infrastructure.exceptionshandler.ExceptionResponse;
import com.pragma.gateway360.shared.constants.GatewayConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
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

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUriFromProperties;

    private final ObjectMapper objectMapper;

    @Value("${app.allowed-origins}")
    private String allowedOrigins;

    public WebSecurityConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public JwtDecoder jwtDecoder(LoadBalancerClient loadBalancerClient) {
        if (!StringUtils.hasText(jwkSetUriFromProperties)) {
            log.error("CRITICAL: API Gateway - JWK Set URI ('spring.security.oauth2.resourceserver.jwt.jwk-set-uri') is MISSING or EMPTY.");
            throw new IllegalStateException("JWK Set URI is missing. Check 'spring.security.oauth2.resourceserver.jwt.jwk-set-uri' property.");
        }

        String finalUriToUse = jwkSetUriFromProperties;

        if (jwkSetUriFromProperties.startsWith("lb://")) {
            log.info("JWK Set URI starts with 'lb://'. Attempting to resolve using LoadBalancerClient: {}", jwkSetUriFromProperties);
            try {
                URI originalUri = new URI(jwkSetUriFromProperties);
                String serviceId = originalUri.getHost();

                if (serviceId == null) {
                    log.error("Invalid lb:// URI: {} - serviceId (host) is null.", jwkSetUriFromProperties);
                    throw new IllegalStateException("Invalid lb:// URI: " + jwkSetUriFromProperties + " - serviceId (host) is null.");
                }

                String serviceIdForLookup = serviceId.toUpperCase();
                log.info("Attempting to choose instance for serviceId '{}' (derived from {}).", serviceIdForLookup, jwkSetUriFromProperties);
                ServiceInstance instance = loadBalancerClient.choose(serviceIdForLookup);

                if (instance == null) {
                    log.error("Service instance NOT FOUND for serviceId: '{}'. Check Eureka registration and ensure the service name in 'jwk-set-uri' ('{}') matches the registered name (case-sensitive, often uppercase in Eureka).",
                            serviceIdForLookup, serviceId);
                    throw new IllegalStateException("Could not find instance of service " + serviceIdForLookup + " for JWK Set URI " + jwkSetUriFromProperties);
                }

                String path = originalUri.getPath() != null ? originalUri.getPath() : "";
                String query = originalUri.getQuery() != null ? "?" + originalUri.getQuery() : "";
                String fragment = originalUri.getFragment() != null ? "#" + originalUri.getFragment() : "";

                finalUriToUse = instance.getUri().toString() + path + query + fragment;
                log.info("Successfully resolved JWK Set URI from '{}' to '{}' using LoadBalancerClient for serviceId '{}'.", jwkSetUriFromProperties, finalUriToUse, serviceId);

            } catch (java.net.URISyntaxException e) {
                log.error("Invalid lb:// URI syntax for JWK Set URI: {}. Error: {}", jwkSetUriFromProperties, e.getMessage(), e);
                throw new IllegalStateException("Invalid lb:// URI syntax for JWK Set URI: " + jwkSetUriFromProperties, e);
            } catch (Exception e) {
                log.error("Unexpected error resolving lb:// URI for JWK Set: {}. Error: {}", jwkSetUriFromProperties, e.getMessage(), e);
                throw new IllegalStateException("Unexpected error resolving lb:// URI: " + jwkSetUriFromProperties, e);
            }
        } else {
            log.info("Using direct JWK Set URI (not starting with lb://): {}", finalUriToUse);
        }

        return NimbusJwtDecoder.withJwkSetUri(finalUriToUse).build();
    }

    @Bean
    public SecurityFilterChain customApiGatewaySecurityFilterChain(
            HttpSecurity http,
            ClaimsToHeadersFilter claimsToHeadersFilter,
            JwtDecoder jwtDecoder) throws Exception {
        log.info("Configuring SecurityFilterChain for API Gateway (customApiGatewaySecurityFilterChain)...");
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/actuator/**",
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
                        .jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder))
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
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        if (StringUtils.hasText(allowedOrigins)) {
            List<String> allowedOriginList = Arrays.stream(allowedOrigins.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
            configuration.setAllowedOrigins(allowedOriginList);
            log.info("CORS configured for origins: {}", allowedOriginList);
        } else {
            log.warn("No explicit origins configured for CORS ('app.allowed-origins' is empty). Permitting all origins ('*'). This is not recommended for production.");
            configuration.setAllowedOrigins(List.of("*"));
        }
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers", "X-User-Id", "X-User-Roles"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type", "X-User-Id", "X-User-Roles"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        log.info("CORS configuration source registered for all paths ('/**').");
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
            response.setStatus(HttpStatus.UNAUTHORIZED.value());

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
                            .map(e -> e.getDescription() + " (Error Code: " + e.getErrorCode() + ")")
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
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            } catch (IOException e) {
                log.error(GatewayConstants.LOG_ERROR_WRITING_AUTH_RESPONSE, e);
            }
        };
    }
}