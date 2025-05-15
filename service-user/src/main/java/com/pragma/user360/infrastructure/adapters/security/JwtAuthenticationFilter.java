package com.pragma.user360.infrastructure.adapters.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;
    private final String jwtHeader;
    private final String jwtPrefix;

    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/api/v1/auth/sign-in",
            "/api/v1/auth/sign-up",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public JwtAuthenticationFilter(
            JwtTokenProvider tokenProvider,
            UserDetailsService userDetailsService,
            @Value("${app.jwt.header:Authorization}") String jwtHeader,
            @Value("${app.jwt.prefix:Bearer }") String jwtPrefix
    ) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
        this.jwtHeader = jwtHeader;
        this.jwtPrefix = jwtPrefix;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestPath = request.getServletPath();

        if (isPublicPath(requestPath)) {
            log.trace("Request path {} is public, skipping JWT filter.", requestPath);
            filterChain.doFilter(request, response);
            return;
        }

        log.trace("Request path {} is protected, processing JWT filter.", requestPath);
        try {
            String jwt = extractJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                String username = tokenProvider.getUsernameFromJWT(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("User '{}' authenticated successfully via JWT for path: {}.", username, requestPath);
            } else {
                handleInvalidOrMissingToken(jwt, requestPath);
            }
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context for path {}: {}", requestPath, ex.getMessage(), ex);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicPath(String requestPath) {
        return PUBLIC_PATHS.stream().anyMatch(pattern -> pathMatcher.match(pattern, requestPath));
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(jwtHeader);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(jwtPrefix)) {
            return bearerToken.substring(jwtPrefix.length());
        }
        return null;
    }

    private void handleInvalidOrMissingToken(String jwt, String requestPath) {
        if (StringUtils.hasText(jwt)) {
            log.warn("Invalid JWT token received for protected path: {}", requestPath);
        } else {
            log.trace("No JWT token found in request header for protected path: {}", requestPath);
        }
    }
}
