package com.pragma.user360.infrastructure.adapters.security;

import com.pragma.user360.domain.model.UserModel;
import com.pragma.user360.domain.ports.out.UserPersistencePort;
import com.pragma.user360.infrastructure.adapters.persistence.UserPersistenceAdapter;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {
    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);
    private static final int MIN_SECRET_LENGTH = 64;
    private final UserPersistencePort userPersistencePort;
    private final UserPersistenceAdapter userPersistenceAdapter;

    @Value("${app.jwt.secret}")
    private String jwtSecretString;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationInMs;

    private SecretKey jwtSecretKey;

    public JwtTokenProvider(UserPersistencePort userPersistencePort, UserPersistenceAdapter userPersistenceAdapter) {
        this.userPersistencePort = userPersistencePort;
        this.userPersistenceAdapter = userPersistenceAdapter;
    }

    @PostConstruct
    public void init() {
        if (!StringUtils.hasText(jwtSecretString)) {
            log.error("JWT Secret is missing! Please configure 'app.jwt.secret' in application.properties");
            throw new IllegalStateException("JWT Secret is missing");
        }

        byte[] keyBytes = jwtSecretString.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < MIN_SECRET_LENGTH) {
            log.error("JWT Secret is too short! Must be at least {} bytes. Current length: {} bytes",
                    MIN_SECRET_LENGTH, keyBytes.length);
            throw new IllegalStateException("JWT Secret is too short");
        }

        try {
            this.jwtSecretKey = Keys.hmacShaKeyFor(keyBytes);
            log.info("JWT Secret key initialized successfully");
        } catch (Exception e) {
            log.error("Error initializing JWT Secret key: {}", e.getMessage());
            throw new IllegalStateException("Error initializing JWT Secret key", e);
        }
    }

    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        UserModel userModel = userPersistenceAdapter.getUserByEmail(userPrincipal.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        log.debug("Generating token for user: {}, roles: {}", userPrincipal.getUsername(), roles);

        return Jwts.builder()
                .subject(userModel.getId().toString())
                .issuedAt(now)
                .expiration(expiryDate)
                .claim("role", roles.get(0))
                .signWith(jwtSecretKey)
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        if (!StringUtils.hasText(token)) {
            throw new IllegalArgumentException("JWT token cannot be null or empty");
        }

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(jwtSecretKey)
                    .build()
                    .parseSignedClaims(token.trim())
                    .getPayload();
            String userId = claims.getSubject();

            // Fetch the user by ID
            UserModel userModel = userPersistenceAdapter.getUserById(Long.valueOf(userId))
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            return userModel.getEmail();


        } catch (Exception e) {
            log.error("Error extracting username from JWT token: {}", e.getMessage());
            throw new JwtException("Error extracting username from JWT token", e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<String> getRolesFromJWT(String token) {
        if (!StringUtils.hasText(token)) {
            throw new IllegalArgumentException("JWT token cannot be null or empty");
        }

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(jwtSecretKey)
                    .build()
                    .parseSignedClaims(token.trim())
                    .getPayload();
            return claims.get("roles", List.class);
        } catch (Exception e) {
            log.error("Error extracting roles from JWT token: {}", e.getMessage());
            throw new JwtException("Error extracting roles from JWT token", e);
        }
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            log.warn("Token validation failed: token is null or empty");
            return false;
        }

        try {
            Jwts.parser()
                    .verifyWith(jwtSecretKey)
                    .build()
                    .parseSignedClaims(token.trim());
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty: {}", ex.getMessage());
        } catch (Exception ex) {
            log.error("Unexpected error validating JWT token: {}", ex.getMessage());
        }
        return false;
    }
}
