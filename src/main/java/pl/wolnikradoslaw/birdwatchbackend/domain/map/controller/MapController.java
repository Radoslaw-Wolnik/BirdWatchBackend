package pl.wolnikradoslaw.birdwatchbackend.domain.map.controller;

import com.example.birdwatchbackend.domain.map.dto.*;
import pl.wolnikradoslaw.birdwatchbackend.domain.map.dto.HabitatAreaResponse;
import pl.wolnikradoslaw.birdwatchbackend.domain.map.dto.HotspotResponse;
import pl.wolnikradoslaw.birdwatchbackend.domain.map.dto.RecentSightingResponse;
import pl.wolnikradoslaw.birdwatchbackend.domain.map.service.MapService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/map")
public class MapController {

    private final MapService mapService;

    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    @GetMapping("/recent-sightings")
    public ResponseEntity<List<RecentSightingResponse>> recentSightings(
            @RequestParam double north,
            @RequestParam double south,
            @RequestParam double east,
            @RequestParam double west,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant since) {
        return ResponseEntity.ok(mapService.getRecentSightings(north, south, east, west, since));
    }

    @GetMapping("/hotspots")
    public ResponseEntity<List<HotspotResponse>> hotspots(
            @RequestParam(required = false) UUID speciesId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @RequestParam double north,
            @RequestParam double south,
            @RequestParam double east,
            @RequestParam double west) {
        return ResponseEntity.ok(mapService.getHotspots(speciesId, from, to, north, south, east, west));
    }

    @GetMapping("/habitats")
    public ResponseEntity<List<HabitatAreaResponse>> habitats(@RequestParam UUID birdSpeciesId) {
        return ResponseEntity.ok(mapService.getHabitats(birdSpeciesId));
    }
}