package pl.wolnikradoslaw.birdwatchbackend.domain.pokedex.dto;

import java.time.Instant;
import java.util.UUID;

public record PokedexEntryResponse(
        UUID birdSpeciesId,
        String commonName,
        String scientificName,
        String slug,                     // for building links
        String rarity,
        String habitatDescription,       // simplified for grouping
        boolean seen,
        boolean seenManually,
        int seenCount,
        Instant firstSeenAt,
        Instant lastSeenAt,
        Double lastSeenLatitude,
        Double lastSeenLongitude
) {}