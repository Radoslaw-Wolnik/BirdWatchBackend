package pl.wolnikradoslaw.birdwatchbackend.domain.trip.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

public record AddObservationRequest(
        @NotNull UUID birdSpeciesId,
        @NotNull Instant observedAt,
        @NotNull Double latitude,
        @NotNull Double longitude,
        Double locationAccuracyMeters,
        @Size(max = 500) String note,
        String photoObjectKey,
        String soundObjectKey,
        boolean manualSeenMark
) {}