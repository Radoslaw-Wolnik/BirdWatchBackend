package com.example.birdwatchbackend.domain.user.service;

import com.example.birdwatchbackend.domain.user.dto.UpdateProfileRequest;
import com.example.birdwatchbackend.domain.user.dto.UserProfileResponse;
import java.util.UUID;

public interface UserService {
    UserProfileResponse getProfile(UUID userId);
    UserProfileResponse updateProfile(UUID userId, UpdateProfileRequest request);
}