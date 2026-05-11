package pl.wolnikradoslaw.birdwatchbackend.domain.map.dto;

public record HotspotResponse(
        Double latitude,
        Double longitude,
        int weight,
        String birdSpeciesId,    // null if aggregated for all species
        String birdSpeciesName
) {}