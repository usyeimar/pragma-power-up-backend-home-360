package com.pragma.user360.infrastructure.adapters.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.pragma.user360.domain.model.UserModel;
import com.pragma.user360.domain.ports.out.UserPersistencePort;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final UserPersistencePort userPersistencePort;

    @Value("${app.jwt.rsa.private-key-path}")
    private Resource rsaPrivateKeyResource;

    @Value("${app.jwt.rsa.public-key-path}")
    private Resource rsaPublicKeyResource;

    @Value("${app.jwt.rsa.key-id}")
    private String rsaKeyId;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationInMs;

    @Value("${app.jwt.issuer-uri:http://user-microservice}")
    private String jwtIssuerUri;

    private RSAPrivateKey rsaPrivateKey;
    private RSAPublicKey rsaPublicKey;
    private RSASSASigner signer;
    private RSASSAVerifier verifier;

    public JwtTokenProvider(UserPersistencePort userPersistencePort) {
        this.userPersistencePort = userPersistencePort;
    }

    @PostConstruct
    public void init() {
        try {
            this.rsaPrivateKey = loadPrivateKey(rsaPrivateKeyResource);
            this.rsaPublicKey = loadPublicKey(rsaPublicKeyResource);
            this.signer = new RSASSASigner(this.rsaPrivateKey);
            this.verifier = new RSASSAVerifier(this.rsaPublicKey);
            log.info("JWT components (Signer/Verifier) initialized successfully for RS256 with Key ID: {}", rsaKeyId);
            log.info("JWT Issuer URI: {}", jwtIssuerUri);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("Error initializing RSA JWT components: {}", e.getMessage(), e);
            throw new IllegalStateException("Error initializing RSA JWT components", e);
        }
    }

    private RSAPrivateKey loadPrivateKey(Resource resource) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        try (InputStream keyStream = resource.getInputStream()) {
            String pemEncodedKey = new String(keyStream.readAllBytes(), StandardCharsets.UTF_8);
            String privateKeyPEM = pemEncodedKey
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replaceAll(System.lineSeparator(), "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] encodedBytes = Base64.getDecoder().decode(privateKeyPEM);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) kf.generatePrivate(keySpec);
        }
    }

    private RSAPublicKey loadPublicKey(Resource resource) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        try (InputStream keyStream = resource.getInputStream()) {
            String pemEncodedKey = new String(keyStream.readAllBytes(), StandardCharsets.UTF_8);
            String publicKeyPEM = pemEncodedKey
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replaceAll(System.lineSeparator(), "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] encodedBytes = Base64.getDecoder().decode(publicKeyPEM);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) kf.generatePublic(keySpec);
        }
    }

    public record TokenDetails(String token, Date issuedAtDate) {}

    public TokenDetails generateTokenDetails(Authentication authentication) {
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

        log.debug("Generating RS256 token for user: {}, roles: {}", userPrincipal.getUsername(), roles);

        JWTClaimsSet payload = new JWTClaimsSet.Builder()
                .subject(userModel.getId().toString())
                .issuer(jwtIssuerUri)
                .issueTime(now)
                .expirationTime(expiryDate)
                .claim("role", roles.isEmpty() ? null : roles.get(0))
                .claim("email", userModel.getEmail())
                .build();

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .keyID(this.rsaKeyId)
                .type(JOSEObjectType.JWT)
                .build();

        SignedJWT signedJWT = new SignedJWT(header, payload);

        try {
            signedJWT.sign(this.signer);
        } catch (JOSEException e) {
            log.error("Error signing JWT with RS256: {}", e.getMessage(), e);
            throw new RuntimeException("Error signing JWT with RS256", e);
        }

        return new TokenDetails(signedJWT.serialize(), now);
    }

    public String getUsernameFromJWT(String token) {
        if (!StringUtils.hasText(token)) {
            throw new IllegalArgumentException("JWT token cannot be null or empty");
        }
        try {
            SignedJWT signedJWT = SignedJWT.parse(token.trim());
            if (!signedJWT.verify(this.verifier)) {
                log.warn("JWT signature verification failed for RS256!");
                throw new JOSEException("JWT signature verification failed for RS256!");
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
            log.error("Error processing RS256 JWT: {}", e.getMessage());
            throw new RuntimeException("Error processing RS256 JWT", e);
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
                log.warn("Invalid JWT signature (RS256). Token: {}", token);
                return false;
            }

            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            Date now = new Date();

            Date expirationTime = claimsSet.getExpirationTime();
            if (expirationTime == null || expirationTime.before(now)) {
                log.warn("Expired JWT token. Expiration: {}, Current: {}. Token: {}", expirationTime, now, token);
                return false;
            }

            String issuer = claimsSet.getIssuer();
            if (!jwtIssuerUri.equals(issuer)) {
                log.warn("Invalid JWT issuer. Expected: {}, Actual: {}. Token: {}", jwtIssuerUri, issuer, token);
                return false;
            }

            log.trace("Token validated successfully (RS256).");
            return true;
        } catch (ParseException e) {
            log.warn("Invalid JWT token (malformed): {}. Token: {}", e.getMessage(), token);
        } catch (JOSEException e) {
            log.warn("JOSE error during JWT validation (RS256): {}. Token: {}", e.getMessage(), token);
        } catch (Exception ex) {
            log.error("Unexpected error validating RS256 JWT token: {}. Token: {}", ex.getMessage(), token, ex);
        }
        return false;
    }

    public RSAKey getRsaPublicKeyAsJwk() {
        if (this.rsaPublicKey == null || this.rsaKeyId == null) {
            log.warn("RSA Public Key or Key ID is not initialized, cannot generate JWK.");
            return null;
        }
        return new RSAKey.Builder(this.rsaPublicKey)
                .keyID(this.rsaKeyId)
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .build();
    }
}
