package pl.wolnikradoslaw.birdwatchbackend.domain.trip.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTripRequest(
        @NotBlank @Size(max = 150) String title,
        @Size(max = 500) String description
) {}