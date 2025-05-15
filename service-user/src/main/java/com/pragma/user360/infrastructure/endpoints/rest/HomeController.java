package com.pragma.user360.infrastructure.endpoints.rest;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Hidden
@RestController
public class HomeController {

    @GetMapping("/")

    public ResponseEntity<Void> redirectToSwagger() {
        return ResponseEntity.status(302).header("Location", "/swagger-ui.html").build();
    }
} 