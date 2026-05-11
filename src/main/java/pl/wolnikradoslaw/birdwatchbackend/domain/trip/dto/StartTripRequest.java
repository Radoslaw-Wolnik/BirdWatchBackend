package pl.wolnikradoslaw.birdwatchbackend.domain.trip.dto;

public record StartTripRequest(
        Double latitude,
        Double longitude
) {}