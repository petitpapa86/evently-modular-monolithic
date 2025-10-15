package com.evently.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PermissionAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Get the current authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            // Extract user permissions from JWT token authorities
            Set<String> userPermissions = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());

            // Check if the request requires specific permissions
            String[] requiredPermissions = getRequiredPermissions(request);

            if (requiredPermissions != null && requiredPermissions.length > 0) {
                boolean hasPermission = Arrays.stream(requiredPermissions)
                        .anyMatch(userPermissions::contains);

                if (!hasPermission) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("{\"error\":\"Insufficient permissions\"}");
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String[] getRequiredPermissions(HttpServletRequest request) {
        // This is a simplified approach. In a real implementation, you would need to
        // match the request against the RouterFunctions to find the required permissions.
        // For now, we'll check for a custom header or attribute that might be set by routing.

        // Check if permissions are stored in request attributes (this would need to be set by routing logic)
        Object permissions = request.getAttribute("permissions");
        if (permissions instanceof String[]) {
            return (String[]) permissions;
        }

        // For demonstration, check common patterns in URLs
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        // Enhanced URL pattern matching based on the actual endpoints
        if ("POST".equals(method) && requestURI.startsWith("/attendees")) {
            return new String[]{"attendees:create"};
        }
        if ("GET".equals(method) && requestURI.startsWith("/attendees")) {
            return new String[]{"attendees:read"};
        }
        if ("GET".equals(method) && requestURI.startsWith("/events")) {
            return new String[]{"events:read"};
        }
        if ("POST".equals(method) && requestURI.startsWith("/events")) {
            return new String[]{"events:create"};
        }
        if ("PUT".equals(method) && requestURI.startsWith("/events/")) {
            return new String[]{"events:write"};
        }
        if ("DELETE".equals(method) && requestURI.startsWith("/events/")) {
            return new String[]{"events:delete"};
        }
        if ("GET".equals(method) && requestURI.startsWith("/users/permissions")) {
            return new String[]{"users:read"};
        }
        if ("GET".equals(method) && requestURI.startsWith("/users/profile")) {
            return new String[]{"users:read"};
        }
        if ("PUT".equals(method) && requestURI.startsWith("/users/profile")) {
            return new String[]{"users:write"};
        }
        if ("GET".equals(method) && requestURI.startsWith("/orders")) {
            return new String[]{"orders:read"};
        }
        if ("POST".equals(method) && requestURI.startsWith("/ticketing/orders")) {
            return new String[]{"orders:create"};
        }
        if ("PUT".equals(method) && requestURI.startsWith("/orders/")) {
            return new String[]{"orders:write"};
        }
        if ("POST".equals(method) && requestURI.startsWith("/ticketing/cart/items")) {
            return new String[]{"cart:create"};
        }
        if ("POST".equals(method) && requestURI.matches("/ticketing/orders/[^/]+/payment")) {
            return new String[]{"payment:create"};
        }
        if ("GET".equals(method) && requestURI.matches("/events/[^/]+/statistics")) {
            return new String[]{"events:read"};
        }

        return null; // No specific permissions required
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();

        // Skip filtering for public endpoints
        return requestURI.startsWith("/auth/token") ||
               requestURI.startsWith("/users/register") ||
               requestURI.startsWith("/actuator/") ||
               requestURI.startsWith("/swagger") ||
               requestURI.startsWith("/v3/api-docs");
    }
}