package pl.wolnikradoslaw.birdwatchbackend.domain.map.dto;

import java.util.UUID;

public record HabitatAreaResponse(
        UUID id,
        String name,
        String description,
        String geometryWkt   // well-known text for client-side rendering (GeoJSON later if needed)
) {}