package com.example.birdwatchbackend.domain.user.dto;

public record TokenResponse(
        String accessToken,
        String tokenType,
        Long expiresIn
) {}