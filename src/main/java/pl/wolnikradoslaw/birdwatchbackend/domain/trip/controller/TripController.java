package pl.wolnikradoslaw.birdwatchbackend.domain.trip.controller;

import com.example.birdwatchbackend.domain.trip.dto.*;
import pl.wolnikradoslaw.birdwatchbackend.domain.trip.dto.*;
import pl.wolnikradoslaw.birdwatchbackend.domain.trip.service.TripService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping
    public ResponseEntity<TripResponse> create(@AuthenticationPrincipal Jwt jwt,
                                               @Valid @RequestBody CreateTripRequest request) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.status(HttpStatus.CREATED).body(tripService.createTrip(userId, request));
    }

    @PostMapping("/{tripId}/start")
    public ResponseEntity<TripResponse> start(@AuthenticationPrincipal Jwt jwt,
                                              @PathVariable UUID tripId,
                                              @RequestBody(required = false) StartTripRequest request) {
        // Use empty if null
        StartTripRequest req = request != null ? request : new StartTripRequest(null, null);
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(tripService.startTrip(userId, tripId, req));
    }

    @PostMapping("/{tripId}/observations")
    public ResponseEntity<TripResponse> addObservation(@AuthenticationPrincipal Jwt jwt,
                                                       @PathVariable UUID tripId,
                                                       @Valid @RequestBody AddObservationRequest request) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.status(HttpStatus.CREATED).body(tripService.addObservation(userId, tripId, request));
    }

    @PostMapping("/{tripId}/end")
    public ResponseEntity<TripResponse> endTrip(@AuthenticationPrincipal Jwt jwt,
                                                @PathVariable UUID tripId,
                                                @Valid @RequestBody EndTripRequest request) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(tripService.endTrip(userId, tripId, request));
    }

    @GetMapping
    public ResponseEntity<List<TripResponse>> listTrips(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(tripService.listUserTrips(userId));
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<TripResponse> getTrip(@AuthenticationPrincipal Jwt jwt,
                                                @PathVariable UUID tripId) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(tripService.getTrip(userId, tripId));
    }

    @DeleteMapping("/{tripId}")
    public ResponseEntity<Void> cancelTrip(@AuthenticationPrincipal Jwt jwt,
                                           @PathVariable UUID tripId) {
        UUID userId = UUID.fromString(jwt.getSubject());
        tripService.cancelTrip(userId, tripId);
        return ResponseEntity.noContent().build();
    }
}