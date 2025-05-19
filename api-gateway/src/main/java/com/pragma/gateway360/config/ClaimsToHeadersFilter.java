package com.pragma.gateway360.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.*;


@Component
@Order(1)
public class ClaimsToHeadersFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(ClaimsToHeadersFilter.class);


    public ClaimsToHeadersFilter() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
            CustomHttpServletRequestWrapper requestWrapper = new CustomHttpServletRequestWrapper(httpRequest);

            // Extrae el User ID del 'subject' del token
            String userId = jwtPrincipal.getSubject();
            if (userId != null) {
                log.debug("API Gateway: Añadiendo cabecera X-User-Id: {}", userId);
                requestWrapper.putHeader("X-User-Id", userId);
            }

            // Extrae el rol del claim 'role'. Asume que es un solo string.
            // Tu JwtTokenProvider en service-user parece añadirlo como: .claim("role", roles.get(0))
            String role = jwtPrincipal.getClaimAsString("role");
            if (role != null && !role.isEmpty()) {
                log.debug("API Gateway: Añadiendo cabecera X-User-Roles: {}", role);
                requestWrapper.putHeader("X-User-Roles", role);
            }

            // Opcional: Extraer el email si lo incluyes en los claims del JWT
            // String email = jwtPrincipal.getClaimAsString("email"); // Asegúrate de que el claim se llame "email"
            // if (email != null && !email.isEmpty()) {
            //     log.debug("API Gateway: Añadiendo cabecera X-User-Email: {}", email);
            //     requestWrapper.putHeader("X-User-Email", email);
            // }

            log.trace("API Gateway: Reenviando solicitud con cabeceras de usuario añadidas.");
            chain.doFilter(requestWrapper, response); // Continúa con la solicitud modificada
        } else {
            // Si no hay un principal Jwt (ej. ruta pública, o fallo de autenticación previo)
            log.trace("API Gateway: No hay Jwt principal en SecurityContext para la ruta: {}. Se procede sin añadir cabeceras de usuario.", httpRequest.getRequestURI());
            chain.doFilter(request, response); // Continúa con la solicitud original
        }
    }

    /**
     * Clase interna Wrapper para poder modificar las cabeceras de la HttpServletRequest.
     * HttpServletRequest es inmutable por defecto.
     */
    private static class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {
        private final Map<String, String> customHeaders;

        public CustomHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
            this.customHeaders = new HashMap<>();
        }

        public void putHeader(String name, String value) {
            this.customHeaders.put(name, value);
        }

        @Override
        public String getHeader(String name) {
            String headerValue = customHeaders.get(name);
            if (headerValue != null) {
                return headerValue;
            }
            return super.getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            Set<String> set = new HashSet<>(customHeaders.keySet());
            Enumeration<String> e = super.getHeaderNames();
            while (e.hasMoreElements()) {
                set.add(e.nextElement());
            }
            return Collections.enumeration(set);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            List<String> values = new ArrayList<>();
            if (customHeaders.containsKey(name)) {
                values.add(customHeaders.get(name));
            }
            Enumeration<String> underlyingHeaders = super.getHeaders(name);
            if (underlyingHeaders != null) {
                while (underlyingHeaders.hasMoreElements()) {
                    values.add(underlyingHeaders.nextElement());
                }
            }
            if (values.isEmpty()) {
                return Collections.emptyEnumeration();
            }
            return Collections.enumeration(values);
        }
    }

    // Métodos init() y destroy() de la interfaz Filter (pueden dejarse vacíos si no se necesitan)
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("ClaimsToHeadersFilter inicializado.");
    }

    @Override
    public void destroy() {
        log.info("ClaimsToHeadersFilter destruido.");
    }
}
