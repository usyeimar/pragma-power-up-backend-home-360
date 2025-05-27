// =================================================================
// service-user/src/main/java/com/pragma/user360/infrastructure/adapters/security/JwtTokenProvider.java
// (Versión con Nimbus para HS512)
// =================================================================
package com.pragma.user360.infrastructure.adapters.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.pragma.user360.domain.model.UserModel;
import com.pragma.user360.domain.ports.out.UserPersistencePort;
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
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.ECKey;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);
    private static final int RECOMMENDED_HS512_SECRET_LENGTH_BYTES = 128;

    private final UserPersistencePort userPersistencePort;

    @Value("${app.jwt.secret}")
    private String jwtSecretString;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationInMs;

    @Value("${app.jwt.issuer-uri:http://user-microservice}")
    private String jwtIssuerUri;

    private MACSigner signer;
    private MACVerifier verifier;

    public JwtTokenProvider(UserPersistencePort userPersistencePort) {
        this.userPersistencePort = userPersistencePort;
    }

    @PostConstruct
    public void init() {
        if (!StringUtils.hasText(jwtSecretString)) {
            log.error("JWT Secret is missing! Please configure 'app.jwt.secret'");
            throw new IllegalStateException("JWT Secret is missing");
        }

        byte[] keyBytes = jwtSecretString.getBytes(StandardCharsets.UTF_8);

        if (keyBytes.length < RECOMMENDED_HS512_SECRET_LENGTH_BYTES) {
            log.warn("JWT Secret ({} bytes) is shorter than the recommended {} bytes for HS512. Ensure this key is strong enough.",
                    keyBytes.length, RECOMMENDED_HS512_SECRET_LENGTH_BYTES);
        }

        try {
            SecretKey sharedSecretKey = new SecretKeySpec(keyBytes, "HmacSHA512");
            this.signer = new MACSigner(sharedSecretKey);
            this.verifier = new MACVerifier(sharedSecretKey);
            log.info("JWT components (Signer/Verifier) initialized successfully for HmacSHA512.");
            log.info("JWT Issuer URI: {}", jwtIssuerUri);
        } catch (JOSEException e) {
            log.error("Error initializing JWT Signer/Verifier for HmacSHA512: {}", e.getMessage());
            throw new IllegalStateException("Error initializing JWT Signer/Verifier for HmacSHA512", e);
        }
    }

    public record TokenDetails(String token, Date issuedAtDate) {
    }

    public TokenDetails generateTokenDetails(Authentication authentication) throws JOSEException {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        UserModel userModel = userPersistencePort.getUserByEmail(userPrincipal.getUsername())
                .orElseThrow(() -> {
                    log.error("User not found with email: {}", userPrincipal.getUsername());
                    return new IllegalArgumentException("User not found with email: " + userPrincipal.getUsername());
                });

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        log.debug("Generating HS512 token for user: {}, roles: {}", userPrincipal.getUsername(), roles);

        var key = new ECKeyGenerator(Curve.P_256)
                .keyID("user360-key")
                .generate();


        JWTClaimsSet payload = new JWTClaimsSet.Builder()
                .subject(userModel.getId().toString())
                .issuer(jwtIssuerUri)
                .issueTime(now) // iat
                .expirationTime(expiryDate) // exp
                .claim("role", roles.isEmpty() ? null : roles.get(0))
                .claim("email", userModel.getEmail())
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS512), payload);

        try {
            signedJWT.sign(this.signer);
        } catch (JOSEException e) {
            log.error("Error signing JWT: {}", e.getMessage(), e);
            throw new RuntimeException("Error signing JWT", e);
        }

        return new TokenDetails(signedJWT.serialize(), now);
    }


    public String getUsernameFromJWT(String token) { // Devuelve el email
        if (!StringUtils.hasText(token)) {
            throw new IllegalArgumentException("JWT token cannot be null or empty");
        }
        try {
            SignedJWT signedJWT = SignedJWT.parse(token.trim());
            if (!signedJWT.verify(this.verifier)) {
                throw new JOSEException("JWT signature verification failed!");
            }
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

            String email = claimsSet.getStringClaim("email");
            if (email != null) {
                return email;
            }

            String userId = claimsSet.getSubject();
            if (userId == null) {
                log.error("Subject (user ID) not found in JWT");
                throw new JOSEException("Subject (user ID) not found in JWT");
            }
            UserModel userModel = userPersistencePort.getUserById(Long.valueOf(userId))
                    .orElseThrow(() -> {
                        log.error("User not found with ID: {}", userId);
                        return new JOSEException("User not found with ID: " + userId);
                    });
            return userModel.getEmail();

        } catch (ParseException e) {
            log.error("Invalid JWT token (malformed): {}", e.getMessage());
            throw new RuntimeException("Invalid JWT token (malformed)", e);
        } catch (JOSEException e) {
            log.error("Error processing JWT (expected HS512): {}", e.getMessage());
            throw new RuntimeException("Error processing JWT (expected HS512)", e);
        }
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            log.warn("Token validation failed: token is null or empty");
            return false;
        }
        try {
            SignedJWT signedJWT = SignedJWT.parse(token.trim());

            if (!signedJWT.verify(this.verifier)) {
                log.error("Invalid JWT signature (expected HS512)");
                return false;
            }

            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            Date now = new Date();

            Date expirationTime = claimsSet.getExpirationTime();
            if (expirationTime == null || expirationTime.before(now)) {
                log.error("Expired JWT token");
                return false;
            }

            String issuer = claimsSet.getIssuer();
            if (!jwtIssuerUri.equals(issuer)) {
                log.error("Invalid JWT issuer. Expected: {}, Actual: {}", jwtIssuerUri, issuer);
                return false;
            }

            return true;
        } catch (ParseException e) {
            log.error("Invalid JWT token (malformed): {}", e.getMessage());
        } catch (JOSEException e) {
            log.error("JOSE error during JWT validation (e.g., signature verification failed): {}", e.getMessage());
        } catch (Exception ex) {
            log.error("Unexpected error validating JWT token (expected HS512): {}", ex.getMessage(), ex);
        }
        return false;
    }
}
