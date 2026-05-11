package pl.wolnikradoslaw.birdwatchbackend.domain.trip.service;

import pl.wolnikradoslaw.birdwatchbackend.domain.trip.dto.*;

import java.util.List;
import java.util.UUID;

public interface TripService {
    TripResponse createTrip(UUID userId, CreateTripRequest request);
    TripResponse startTrip(UUID userId, UUID tripId, StartTripRequest request);
    TripResponse addObservation(UUID userId, UUID tripId, AddObservationRequest request);
    TripResponse endTrip(UUID userId, UUID tripId, EndTripRequest request);
    TripResponse getTrip(UUID userId, UUID tripId);
    List<TripResponse> listUserTrips(UUID userId);
    void cancelTrip(UUID userId, UUID tripId);
}