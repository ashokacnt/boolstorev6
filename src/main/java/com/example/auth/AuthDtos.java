package com.example.auth;

import jakarta.validation.constraints.NotBlank;

public final class AuthDtos {
    private AuthDtos() {
    }

    public record RegisterRequest(@NotBlank String username, @NotBlank String password) {
    }

    public record UserResponse(Long id, String username, String role) {
    }
}
