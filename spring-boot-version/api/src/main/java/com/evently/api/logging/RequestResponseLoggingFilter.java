package com.evently.api.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    // Headers that contain sensitive information and should not be logged
    private static final List<String> SENSITIVE_HEADERS = Arrays.asList(
        "authorization",
        "x-api-key",
        "cookie",
        "set-cookie"
    );

    // Request paths to exclude from detailed logging
    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
        "/actuator/health",
        "/actuator/info"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);

        Instant startTime = Instant.now();

        // Wrap request and response to capture content
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        try {
            // Log incoming request
            logRequest(requestWrapper, requestId);

            // Process the request
            filterChain.doFilter(requestWrapper, responseWrapper);

            // Log outgoing response
            logResponse(responseWrapper, requestId, startTime);

        } finally {
            // Ensure response is written back to client
            responseWrapper.copyBodyToResponse();
            MDC.clear();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request, String requestId) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        String queryString = request.getQueryString();
        String remoteAddr = getClientIpAddress(request);

        // Skip detailed logging for excluded paths
        if (EXCLUDED_PATHS.stream().anyMatch(path::startsWith)) {
            logger.info("REQUEST [{}] {} {} from {}", requestId, method, path, remoteAddr);
            return;
        }

        logger.info("REQUEST [{}] {} {} from {}", requestId, method, path, remoteAddr);

        // Log headers (excluding sensitive ones)
        logger.debug("REQUEST [{}] Headers:", requestId);
        request.getHeaderNames().asIterator().forEachRemaining(headerName -> {
            if (!SENSITIVE_HEADERS.contains(headerName.toLowerCase())) {
                logger.debug("REQUEST [{}] {}: {}", requestId, headerName, request.getHeader(headerName));
            } else {
                logger.debug("REQUEST [{}] {}: [REDACTED]", requestId, headerName);
            }
        });

        // Log query parameters
        if (queryString != null && !queryString.isEmpty()) {
            logger.debug("REQUEST [{}] Query: {}", requestId, queryString);
        }

        // Log request body for POST/PUT/PATCH requests (limited size)
        if (isBodyLoggingEnabled(method)) {
            byte[] content = request.getContentAsByteArray();
            if (content.length > 0) {
                try {
                    String body = new String(content, request.getCharacterEncoding() != null ?
                        request.getCharacterEncoding() : "UTF-8");

                    // Truncate large bodies
                    if (body.length() > 1000) {
                        body = body.substring(0, 1000) + "... [TRUNCATED]";
                    }

                    // Check for sensitive content in body
                    if (containsSensitiveData(body)) {
                        logger.debug("REQUEST [{}] Body: [REDACTED - Contains sensitive data]", requestId);
                    } else {
                        logger.debug("REQUEST [{}] Body: {}", requestId, body);
                    }
                } catch (UnsupportedEncodingException e) {
                    logger.warn("REQUEST [{}] Failed to decode request body: {}", requestId, e.getMessage());
                }
            }
        }
    }

    private void logResponse(ContentCachingResponseWrapper response, String requestId, Instant startTime) {
        int statusCode = response.getStatus();
        Duration duration = Duration.between(startTime, Instant.now());
        long durationMs = duration.toMillis();

        logger.info("RESPONSE [{}] Status: {} in {}ms", requestId, statusCode, durationMs);

        // Log response headers (excluding sensitive ones)
        logger.debug("RESPONSE [{}] Headers:", requestId);
        response.getHeaderNames().forEach(headerName -> {
            if (!SENSITIVE_HEADERS.contains(headerName.toLowerCase())) {
                logger.debug("RESPONSE [{}] {}: {}", requestId, headerName, response.getHeader(headerName));
            } else {
                logger.debug("RESPONSE [{}] {}: [REDACTED]", requestId, headerName);
            }
        });

        // Log response body for error responses or small successful responses
        if (shouldLogResponseBody(statusCode)) {
            byte[] content = response.getContentAsByteArray();
            if (content.length > 0) {
                try {
                    String body = new String(content, response.getCharacterEncoding() != null ?
                        response.getCharacterEncoding() : "UTF-8");

                    // Truncate large bodies
                    if (body.length() > 2000) {
                        body = body.substring(0, 2000) + "... [TRUNCATED]";
                    }

                    logger.debug("RESPONSE [{}] Body: {}", requestId, body);
                } catch (UnsupportedEncodingException e) {
                    logger.warn("RESPONSE [{}] Failed to decode response body: {}", requestId, e.getMessage());
                }
            }
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }

    private boolean isBodyLoggingEnabled(String method) {
        return Arrays.asList("POST", "PUT", "PATCH").contains(method.toUpperCase());
    }

    private boolean shouldLogResponseBody(int statusCode) {
        // Log response body for client errors (4xx) and server errors (5xx)
        return statusCode >= 400 || (statusCode >= 200 && statusCode < 300 && statusCode != 204);
    }

    private boolean containsSensitiveData(String content) {
        String lowerContent = content.toLowerCase();
        return lowerContent.contains("password") ||
               lowerContent.contains("token") ||
               lowerContent.contains("secret") ||
               lowerContent.contains("key");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // Skip filtering for static resources
        return path.startsWith("/css/") ||
               path.startsWith("/js/") ||
               path.startsWith("/images/") ||
               path.startsWith("/favicon.ico") ||
               path.endsWith(".css") ||
               path.endsWith(".js") ||
               path.endsWith(".png") ||
               path.endsWith(".jpg") ||
               path.endsWith(".jpeg") ||
               path.endsWith(".gif") ||
               path.endsWith(".ico");
    }
}