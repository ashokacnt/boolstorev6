package com.example.config;

import com.example.common.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Centralized JSON error handling for Spring Security failures.
 * Authentication failures are returned as HTTP 401 and authorization failures as HTTP 403.
 */
@Component
public class SecurityErrorHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public SecurityErrorHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        write(response, 401, "UNAUTHORIZED", "Unauthorized",
                "Authentication is required to access this resource. Please provide valid username and password.",
                request.getRequestURI());
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        write(response, 403, "FORBIDDEN", "Forbidden",
                "You are not authorized to perform this operation. ADMIN role is required to add books.",
                request.getRequestURI());
    }

    private void write(HttpServletResponse response,
                       int status,
                       String code,
                       String error,
                       String message,
                       String path) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ApiError apiError = new ApiError(status, code, error, message, path, List.of());
        objectMapper.writeValue(response.getOutputStream(), apiError);
    }
}
