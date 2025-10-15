package com.evently.api.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class TagsLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(TagsLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extract tags from request attributes (this would be set by routing logic in a full implementation)
        Object tags = request.getAttribute("tags");
        String[] tagArray = null;

        if (tags instanceof String[]) {
            tagArray = (String[]) tags;
        } else {
            // Fallback: infer tags from URL patterns
            tagArray = inferTagsFromRequest(request);
        }

        if (tagArray != null && tagArray.length > 0) {
            // Add tags to MDC for structured logging
            String tagsString = String.join(",", tagArray);
            logger.info("Request [{}] {} {} - Tags: {}",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                tagsString);
        }

        filterChain.doFilter(request, response);
    }

    private String[] inferTagsFromRequest(HttpServletRequest request) {
        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/attendees")) {
            return new String[]{"Attendees"};
        } else if (requestURI.startsWith("/events")) {
            return new String[]{"Events"};
        } else if (requestURI.startsWith("/ticketing")) {
            return new String[]{"Ticketing"};
        } else if (requestURI.startsWith("/users")) {
            return new String[]{"Users"};
        }

        return new String[]{"General"};
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();


        // Skip filtering for static resources and health checks
        return requestURI.startsWith("/actuator/") ||
               requestURI.startsWith("/swagger") ||
               requestURI.startsWith("/v3/api-docs") ||
               requestURI.startsWith("/favicon.ico") ||
               requestURI.startsWith("/css/") ||
               requestURI.startsWith("/js/") ||
               requestURI.startsWith("/images/");
    }
}