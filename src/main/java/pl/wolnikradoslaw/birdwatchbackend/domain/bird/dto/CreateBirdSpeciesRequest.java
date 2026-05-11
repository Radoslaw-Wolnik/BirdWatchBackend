package pl.wolnikradoslaw.birdwatchbackend.domain.bird.dto;

import pl.wolnikradoslaw.birdwatchbackend.domain.bird.enums.ActivityPattern;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.enums.BirdRarity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Month;
import java.util.List;

public record CreateBirdSpeciesRequest(
        @NotBlank @Size(max = 120) String commonName,
        @NotBlank @Size(max = 180) String scientificName,
        @NotBlank @Size(max = 140) String slug,
        @Size(max = 100) String family,
        @Size(max = 100) String orderName,
        String description,
        String behaviorDescription,
        String dietDescription,
        String habitatDescription,
        String migrationDescription,
        @NotNull BirdRarity rarity,
        @NotNull ActivityPattern activityPattern,
        boolean migratory,
        List<Month> bestSeenMonths,
        @Size(max = 80) String conservationStatus,
        @Size(max = 120) String audioCallSpeciesName
) {}