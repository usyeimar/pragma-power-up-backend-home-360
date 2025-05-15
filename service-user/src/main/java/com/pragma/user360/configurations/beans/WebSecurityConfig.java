package com.pragma.user360.configurations.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.user360.infrastructure.adapters.security.JwtAuthenticationFilter;
import com.pragma.user360.infrastructure.exceptionshandler.ExceptionResponse;
import com.pragma.user360.infrastructure.utils.constants.InfraestructureConstants; // Importar constantes
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final ObjectMapper objectMapper;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public WebSecurityConfig(ObjectMapper objectMapper, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.objectMapper = objectMapper;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(authenticationEntryPoint()).accessDeniedHandler(accessDeniedHandler())).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(authorize -> authorize.requestMatchers("/").permitAll().requestMatchers("/v3/api-docs/**").permitAll().requestMatchers("/swagger-ui/**").permitAll().requestMatchers("/swagger-ui.html").permitAll().requestMatchers("/swagger-resources/**").permitAll().requestMatchers("/webjars/**").permitAll().requestMatchers(HttpMethod.POST, "/api/v1/auth/sign-up").permitAll().requestMatchers(HttpMethod.POST, "/api/v1/auth/sign-in").permitAll().anyRequest().authenticated());

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ExceptionResponse errorResponse = new ExceptionResponse(false, InfraestructureConstants.ERROR_UNAUTHORIZED_TITLE, LocalDateTime.now(), authException.getMessage() != null ? InfraestructureConstants.ERROR_UNAUTHORIZED_DESCRIPTION : InfraestructureConstants.ERROR_UNAUTHORIZED_DETAIL_GENERIC);
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ExceptionResponse errorResponse = new ExceptionResponse(false, InfraestructureConstants.ERROR_ACCESS_DENIED_TITLE, LocalDateTime.now(), InfraestructureConstants.ERROR_ACCESS_DENIED_DETAIL);
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

}
