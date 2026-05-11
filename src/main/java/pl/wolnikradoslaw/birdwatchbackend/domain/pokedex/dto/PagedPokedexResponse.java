package pl.wolnikradoslaw.birdwatchbackend.domain.pokedex.dto;

import jakarta.validation.constraints.NotNull;

public record MarkSeenRequest(
        @NotNull boolean seenManually   // true = manual mark, false = unmark (remove seen status)
) {}