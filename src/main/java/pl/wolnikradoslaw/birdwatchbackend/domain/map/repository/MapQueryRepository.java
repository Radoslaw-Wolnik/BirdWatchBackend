package pl.wolnikradoslaw.birdwatchbackend.domain.map.repository;

import pl.wolnikradoslaw.birdwatchbackend.domain.map.dto.HotspotResponse;
import pl.wolnikradoslaw.birdwatchbackend.domain.map.dto.RecentSightingResponse;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface MapQueryRepository {
    List<RecentSightingResponse> findRecentObservations(double north, double south, double east, double west, Instant since);
    List<HotspotResponse> computeHotspots(UUID speciesId, Instant start, Instant end,
                                          double north, double south, double east, double west,
                                          double gridSizeDegrees);
}