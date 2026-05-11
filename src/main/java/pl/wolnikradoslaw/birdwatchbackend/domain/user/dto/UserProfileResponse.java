package pl.wolnikradoslaw.birdwatchbackend.domain.user.dto;

import java.util.UUID;

public record UserProfileResponse(
        UUID id,
        String username,
        String email,
        String role,
        String profilePictureUrl,
        Double homeLatitude,
        Double homeLongitude,
        String homeLocationLabel,
        String country,
        String region,
        String timezone
) {}
