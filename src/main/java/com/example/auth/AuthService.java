package com.example.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.common.ConflictException;

@Service
public class AuthService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public AuthService(UserRepository r, PasswordEncoder e) {
        repo = r;
        encoder = e;
    }

    @Transactional
    public AuthDtos.UserResponse register(AuthDtos.RegisterRequest req) {
        if (repo.existsByUsername(req.username()))
            throw new ConflictException("Username already exists: " + req.username());
        User u = repo.save(new User(req.username(), encoder.encode(req.password()), "USER"));
        return new AuthDtos.UserResponse(u.getId(), u.getUsername(), u.getRole());
    }
}
