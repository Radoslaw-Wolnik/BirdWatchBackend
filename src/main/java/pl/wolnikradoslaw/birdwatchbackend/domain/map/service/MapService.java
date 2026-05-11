package pl.wolnikradoslaw.birdwatchbackend.domain.map.service;

import com.example.birdwatchbackend.domain.map.dto.*;
import pl.wolnikradoslaw.birdwatchbackend.domain.map.dto.HabitatAreaResponse;
import pl.wolnikradoslaw.birdwatchbackend.domain.map.dto.HotspotResponse;
import pl.wolnikradoslaw.birdwatchbackend.domain.map.dto.RecentSightingResponse;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface MapService {
    List<RecentSightingResponse> getRecentSightings(double north, double south, double east, double west, Instant since);
    List<HotspotResponse> getHotspots(UUID speciesId, Instant from, Instant to,
                                      double north, double south, double east, double west);
    List<HabitatAreaResponse> getHabitats(UUID birdSpeciesId);
}