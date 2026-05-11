package pl.wolnikradoslaw.birdwatchbackend.domain.bird.dto;

import pl.wolnikradoslaw.birdwatchbackend.domain.bird.enums.ActivityPattern;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.enums.BirdRarity;
import java.time.Month;
import java.util.List;
import java.util.UUID;

public record BirdSpeciesResponse(
        UUID id,
        String slug,
        String commonName,
        String scientificName,
        String family,
        String orderName,
        String description,
        String behaviorDescription,
        String dietDescription,
        String habitatDescription,
        String migrationDescription,
        BirdRarity rarity,
        ActivityPattern activityPattern,
        boolean migratory,
        List<Month> bestSeenMonths,
        String conservationStatus,
        String audioCallSpeciesName
) {}