package pl.wolnikradoslaw.birdwatchbackend.domain.trip.dto;

import pl.wolnikradoslaw.birdwatchbackend.domain.trip.enums.TripStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record TripResponse(
        UUID id,
        String title,
        String description,
        TripStatus status,
        Instant startedAt,
        Instant endedAt,
        Double startLatitude,
        Double startLongitude,
        Double endLatitude,
        Double endLongitude,
        Double distanceMeters,
        Long durationSeconds,
        List<ObservationResponse> observations
) {}