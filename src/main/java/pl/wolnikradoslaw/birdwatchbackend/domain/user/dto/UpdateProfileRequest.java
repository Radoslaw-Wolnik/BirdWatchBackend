package pl.wolnikradoslaw.birdwatchbackend.domain.user.dto;

import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @Size(min = 3, max = 50) String username,
        @Size(max = 255) String homeLocationLabel,
        Double latitude,
        Double longitude,
        String country,
        String region,
        String timezone
) {}