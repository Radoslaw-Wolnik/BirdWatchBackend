package pl.wolnikradoslaw.birdwatchbackend.domain.user.service;

import pl.wolnikradoslaw.birdwatchbackend.domain.user.dto.LoginRequest;
import pl.wolnikradoslaw.birdwatchbackend.domain.user.dto.RegisterRequest;
import pl.wolnikradoslaw.birdwatchbackend.domain.user.dto.TokenResponse;

public interface AuthService {
    TokenResponse register(RegisterRequest request);
    TokenResponse login(LoginRequest request);
}