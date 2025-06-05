package com.pragma.user360.infrastructure.endpoints.rest;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.pragma.user360.infrastructure.adapters.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@Hidden
public class JwksController {

    private final JwtTokenProvider jwtTokenProvider;

    public JwksController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping(value = "/.well-known/jwks.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> jwks() {
        RSAKey rsaKey = jwtTokenProvider.getRsaPublicKeyAsJwk();
        if (rsaKey != null) {
            return new JWKSet(rsaKey).toJSONObject();
        }
        return new JWKSet(Collections.emptyList()).toJSONObject();
    }
}