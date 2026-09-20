package com.example.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private ApiError e(HttpStatus s, String code, String msg, HttpServletRequest r, List<ApiError.FieldError> f) {
        return new ApiError(s.value(), code, s.getReasonPhrase(), msg, r.getRequestURI(), f);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ApiError> notFound(ResourceNotFoundException x, HttpServletRequest r) {
        return ResponseEntity.status(404).body(e(HttpStatus.NOT_FOUND, "NOT_FOUND", x.getMessage(), r, List.of()));
    }

    @ExceptionHandler(ConflictException.class)
    ResponseEntity<ApiError> conflict(ConflictException x, HttpServletRequest r) {
        return ResponseEntity.status(409).body(e(HttpStatus.CONFLICT, "CONFLICT", x.getMessage(), r, List.of()));
    }

    @ExceptionHandler(BadRequestException.class)
    ResponseEntity<ApiError> bad(BadRequestException x, HttpServletRequest r) {
        return ResponseEntity.badRequest().body(e(HttpStatus.BAD_REQUEST, "BAD_REQUEST", x.getMessage(), r, List.of()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> validation(MethodArgumentNotValidException x, HttpServletRequest r) {
        List<ApiError.FieldError> fs = x.getBindingResult().getFieldErrors().stream().map(f -> new ApiError.FieldError(f.getField(), f.getDefaultMessage())).toList();
        return ResponseEntity.badRequest().body(e(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Request validation failed", r, fs));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ApiError> unreadable(HttpMessageNotReadableException x, HttpServletRequest r) {
        return ResponseEntity.badRequest().body(e(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "Request body is missing or contains invalid JSON/data.", r, List.of()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ApiError> cv(ConstraintViolationException x, HttpServletRequest r) {
        return ResponseEntity.badRequest().body(e(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", x.getMessage(), r, List.of()));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiError> all(Exception x, HttpServletRequest r) {
        return ResponseEntity.status(500).body(e(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "Unexpected server error", r, List.of()));
    }
}
