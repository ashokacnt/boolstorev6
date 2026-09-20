package com.example.common;

import java.util.List;

public record ApiError(int status, String code, String error, String message, String path,
                       List<FieldError> fieldErrors) {
    public record FieldError(String field, String message) {
    }
}
