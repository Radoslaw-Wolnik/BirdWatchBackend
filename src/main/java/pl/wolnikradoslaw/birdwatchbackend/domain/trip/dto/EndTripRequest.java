package pl.wolnikradoslaw.birdwatchbackend.domain.trip.dto;

import jakarta.validation.constraints.Size;

public record EndTripRequest(
        @Size(max = 150) String title,
        @Size(max = 1000) String description,
        Double endLatitude,
        Double endLongitude,
        Double distanceMeters,
        Long durationSeconds
) {}