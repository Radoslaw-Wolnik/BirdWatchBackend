package pl.wolnikradoslaw.birdwatchbackend.domain.map.dto;

import java.time.Instant;

public record RecentSightingResponse(
        String observationId,
        String birdSpeciesId,
        String birdCommonName,
        String username,
        Instant observedAt,
        Double latitude,
        Double longitude,
        String note,
        String photoUrl
) {}