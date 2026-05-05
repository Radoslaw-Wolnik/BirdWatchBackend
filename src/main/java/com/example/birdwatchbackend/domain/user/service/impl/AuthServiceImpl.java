package com.example.birdwatchbackend.domain.user.service.impl;

import com.example.birdwatchbackend.common.security.JwtService;
import com.example.birdwatchbackend.domain.user.User;
import com.example.birdwatchbackend.domain.user.UserRole;
import com.example.birdwatchbackend.domain.user.dto.LoginRequest;
import com.example.birdwatchbackend.domain.user.dto.RegisterRequest;
import com.example.birdwatchbackend.domain.user.dto.TokenResponse;
import com.example.birdwatchbackend.domain.user.repository.UserRepository;
import com.example.birdwatchbackend.domain.user.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    @Override
    public TokenResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already taken");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(UserRole.USER);
        user = userRepository.save(user);

        String token = jwtService.generateToken(user.getId(), user.getEmail(), user.getRole().name());
        return new TokenResponse(token, "Bearer", 86400L);
    }

    @Override
    public TokenResponse login(LoginRequest request) {
        String identifier = request.usernameOrEmail().trim();

        User user = identifier.contains("@")
                ? userRepository.findByEmail(identifier)
                  .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"))
                : userRepository.findByUsername(identifier)
                  .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail(), user.getRole().name());
        return new TokenResponse(token, "Bearer", 86400L);
    }
}