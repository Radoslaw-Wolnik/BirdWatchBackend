package com.example.birdwatchbackend.domain.user.service;

import com.example.birdwatchbackend.domain.user.dto.LoginRequest;
import com.example.birdwatchbackend.domain.user.dto.RegisterRequest;
import com.example.birdwatchbackend.domain.user.dto.TokenResponse;

public interface AuthService {
    TokenResponse register(RegisterRequest request);
    TokenResponse login(LoginRequest request);
}