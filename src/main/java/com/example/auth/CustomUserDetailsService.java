package com.example.auth;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository repo;

    public CustomUserDetailsService(UserRepository r) {
        repo = r;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repo.findByUsername(username).map(u -> User.withUsername(u.getUsername()).password(u.getPassword()).roles(u.getRole()).build()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
