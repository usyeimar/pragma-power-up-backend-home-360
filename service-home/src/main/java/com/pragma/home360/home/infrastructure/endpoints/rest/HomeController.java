package com.pragma.home360.home.infrastructure.endpoints.rest;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
public class HomeController {

    /**
     * Redirects to the Swagger UI page.
     *
     * @return a ResponseEntity with a 302 status code and a Location header pointing to the Swagger UI page.
     */
    @GetMapping("/")
    public ResponseEntity<Void> redirectToSwagger() {
        return ResponseEntity.status(302).header("Location", "/swagger-ui.html").build();
    }
}
