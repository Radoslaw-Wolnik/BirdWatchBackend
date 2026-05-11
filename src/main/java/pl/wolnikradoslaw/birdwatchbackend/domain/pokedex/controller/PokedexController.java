package pl.wolnikradoslaw.birdwatchbackend.domain.pokedex.controller;

import pl.wolnikradoslaw.birdwatchbackend.domain.pokedex.dto.MarkSeenRequest;
import pl.wolnikradoslaw.birdwatchbackend.domain.pokedex.dto.PokedexEntryResponse;
import pl.wolnikradoslaw.birdwatchbackend.domain.pokedex.service.PokedexService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pokedex")
public class PokedexController {

    private final PokedexService pokedexService;

    public PokedexController(PokedexService pokedexService) {
        this.pokedexService = pokedexService;
    }

    @GetMapping
    public ResponseEntity<List<PokedexEntryResponse>> getPokedex(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "name") String groupBy) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(pokedexService.getUserPokedex(userId, groupBy));
    }

    @GetMapping("/unseen")
    public ResponseEntity<List<PokedexEntryResponse>> getUnseen(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(pokedexService.getUnseenBirds(userId));
    }

    @PatchMapping("/{speciesId}/seen")
    public ResponseEntity<Void> markSeen(@AuthenticationPrincipal Jwt jwt,
                                         @PathVariable UUID speciesId,
                                         @Valid @RequestBody MarkSeenRequest request) {
        UUID userId = UUID.fromString(jwt.getSubject());
        pokedexService.markAsSeen(userId, speciesId, request.seenManually());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{speciesId}/seen")
    public ResponseEntity<Void> markUnseen(@AuthenticationPrincipal Jwt jwt,
                                           @PathVariable UUID speciesId) {
        UUID userId = UUID.fromString(jwt.getSubject());
        pokedexService.markAsUnseen(userId, speciesId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{speciesId}/hide")
    public ResponseEntity<Void> hideFromUnseen(@AuthenticationPrincipal Jwt jwt,
                                               @PathVariable UUID speciesId) {
        UUID userId = UUID.fromString(jwt.getSubject());
        pokedexService.hideFromUnseen(userId, speciesId);
        return ResponseEntity.ok().build();
    }
}