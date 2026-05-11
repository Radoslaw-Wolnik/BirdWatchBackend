package pl.wolnikradoslaw.birdwatchbackend.domain.pokedex.service;

import pl.wolnikradoslaw.birdwatchbackend.domain.pokedex.dto.PokedexEntryResponse;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface PokedexService {
    List<PokedexEntryResponse> getUserPokedex(UUID userId, String groupBy);
    List<PokedexEntryResponse> getUnseenBirds(UUID userId);
    void markAsSeen(UUID userId, UUID birdSpeciesId, boolean seenManually);
    void markAsUnseen(UUID userId, UUID birdSpeciesId);
    void hideFromUnseen(UUID userId, UUID birdSpeciesId);

    // Internal – called when a new observation is saved
    void recordObservation(UUID userId, UUID birdSpeciesId, Double latitude, Double longitude, Instant observedAt);
}