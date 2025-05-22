package com.pragma.gateway360.shared.configurations;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

@Component
@Order(1)
public class ClaimsToHeadersFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(ClaimsToHeadersFilter.class);

    private static final String HEADER_USER_ID = "X-User-Id";
    private static final String HEADER_USER_ROLES = "X-User-Roles";
    private static final String CLAIM_ROLE = "role";


    public ClaimsToHeadersFilter() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof Jwt jwtPrincipal) {
            CustomHttpServletRequestWrapper requestWrapper = new CustomHttpServletRequestWrapper(httpRequest);

            String userId = jwtPrincipal.getSubject();
            if (StringUtils.hasText(userId)) {
                log.debug("API Gateway: Adding header {}: {}", HEADER_USER_ID, userId);
                requestWrapper.putHeader(HEADER_USER_ID, userId);
            }

            String role = jwtPrincipal.getClaimAsString(CLAIM_ROLE);
            if (StringUtils.hasText(role)) {
                log.debug("API Gateway: Adding header {}: {}", HEADER_USER_ROLES, role);
                requestWrapper.putHeader(HEADER_USER_ROLES, role);
            }

            chain.doFilter(requestWrapper, response);
        } else {
            log.trace("API Gateway: No Jwt principal in SecurityContext for path: {}. Proceeding without adding user headers.", httpRequest.getRequestURI());
            chain.doFilter(request, response);
        }
    }

    private static class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {
        private final Map<String, String> customHeaders;

        public CustomHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
            this.customHeaders = new HashMap<>();
        }

        public void putHeader(String name, String value) {
            this.customHeaders.put(name.toLowerCase(), value);
        }

        @Override
        public String getHeader(String name) {
            String customHeaderValue = customHeaders.get(name.toLowerCase());
            return customHeaderValue != null ? customHeaderValue : super.getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            Set<String> headerNames = new HashSet<>(customHeaders.keySet());
            Enumeration<String> originalHeaderNames = super.getHeaderNames();
            while (originalHeaderNames.hasMoreElements()) {
                headerNames.add(originalHeaderNames.nextElement());
            }
            return Collections.enumeration(headerNames);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            String customHeaderValue = customHeaders.get(name.toLowerCase());
            List<String> values = new ArrayList<>();

            if (customHeaderValue != null) {
                values.add(customHeaderValue);
            } else {
                // If not overridden, delegate to the original request to get all headers for the name
                Enumeration<String> originalHeaders = super.getHeaders(name);
                if (originalHeaders != null) {
                    values.addAll(Collections.list(originalHeaders));
                }
            }

            if (values.isEmpty() && customHeaderValue == null && !Collections.list(super.getHeaderNames()).contains(name.toLowerCase())) {
                // If the header is not in customHeaders and not in original headers, it doesn't exist.
                // However, if customHeaderValue is null but the header *could* exist in super,
                // an empty enumeration is correct if super.getHeaders(name) returns an empty one.
                // This check ensures we don't return an enumeration for a completely non-existent header
                // unless it was explicitly set to null/empty in customHeaders (which putHeader doesn't allow for null values).
            }


            return Collections.enumeration(values);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("ClaimsToHeadersFilter initialized.");
    }

    @Override
    public void destroy() {
        log.info("ClaimsToHeadersFilter destroyed.");
    }
}
