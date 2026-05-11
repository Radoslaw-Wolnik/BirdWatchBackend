package pl.wolnikradoslaw.birdwatchbackend.domain.bird.dto;

import pl.wolnikradoslaw.birdwatchbackend.domain.bird.enums.ActivityPattern;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.enums.BirdRarity;
import jakarta.validation.constraints.Size;
import java.time.Month;
import java.util.List;

public record UpdateBirdSpeciesRequest(
        @Size(max = 120) String commonName,
        @Size(max = 180) String scientificName,
        @Size(max = 140) String slug,
        @Size(max = 100) String family,
        @Size(max = 100) String orderName,
        String description,
        String behaviorDescription,
        String dietDescription,
        String habitatDescription,
        String migrationDescription,
        BirdRarity rarity,
        ActivityPattern activityPattern,
        Boolean migratory,
        List<Month> bestSeenMonths,
        @Size(max = 80) String conservationStatus,
        String audioCallSpeciesName
) {}