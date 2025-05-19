package com.pragma.gateway360.config;

import jakarta.servlet.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Value("${app.jwt.secret}")
    private String jwtSecretString;


    @Bean
    public SecurityFilterChain springSecurityFilterChain(HttpSecurity http, ClaimsToHeadersFilter claimsToHeadersFilter) throws Exception {
        log.info("Configurando SecurityFilterChain para API Gateway MVC...");
        http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/webjars/**").permitAll()
                        .anyRequest().authenticated()
                ).oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder()))
                ).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable);


        http.addFilterAfter(claimsToHeadersFilter, BearerTokenAuthenticationFilter.class);
        log.info("ClaimsToHeadersFilter añadido a la cadena de seguridad.");

        return http.build();
    }


    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] keyBytes = jwtSecretString.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(secretKey).build();
        log.info("JwtDecoder configurado con HmacSHA256.");
        return decoder;
    }


    @Bean
    public SecretKey sharedSecretKey() {
        byte[] keyBytes = jwtSecretString.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
        log.info("Shared SecretKey Bean creado.");
        return secretKey;
    }
}
