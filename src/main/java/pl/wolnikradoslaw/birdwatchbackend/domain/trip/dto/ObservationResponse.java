package pl.wolnikradoslaw.birdwatchbackend.domain.trip.dto;

import java.time.Instant;
import java.util.UUID;

public record ObservationResponse(
        UUID id,
        UUID birdSpeciesId,
        String birdCommonName,   // added for convenience
        Instant observedAt,
        Double latitude,
        Double longitude,
        Double locationAccuracyMeters,
        String note,
        String photoUrl,         // we'll build from presigned URL
        String soundUrl,
        boolean manualSeenMark,
        Instant createdAt
) {}