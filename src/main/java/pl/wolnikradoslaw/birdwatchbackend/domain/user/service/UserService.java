package pl.wolnikradoslaw.birdwatchbackend.domain.user.service;

import pl.wolnikradoslaw.birdwatchbackend.domain.user.dto.UpdateProfileRequest;
import pl.wolnikradoslaw.birdwatchbackend.domain.user.dto.UserProfileResponse;
import java.util.UUID;

public interface UserService {
    UserProfileResponse getProfile(UUID userId);
    UserProfileResponse updateProfile(UUID userId, UpdateProfileRequest request);
}