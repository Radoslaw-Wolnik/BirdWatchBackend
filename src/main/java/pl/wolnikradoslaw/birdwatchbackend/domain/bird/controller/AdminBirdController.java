package pl.wolnikradoslaw.birdwatchbackend.domain.bird.controller;

import pl.wolnikradoslaw.birdwatchbackend.domain.bird.dto.BirdMediaResponse;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.dto.BirdSpeciesResponse;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.dto.CreateBirdSpeciesRequest;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.dto.UpdateBirdSpeciesRequest;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.service.BirdSpeciesService;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.service.MediaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/birds")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBirdController {

    private final BirdSpeciesService speciesService;
    private final MediaService mediaService;

    public AdminBirdController(BirdSpeciesService speciesService, MediaService mediaService) {
        this.speciesService = speciesService;
        this.mediaService = mediaService;
    }

    @PostMapping
    public ResponseEntity<BirdSpeciesResponse> create(@Valid @RequestBody CreateBirdSpeciesRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(speciesService.create(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BirdSpeciesResponse> update(@PathVariable UUID id,
                                                      @Valid @RequestBody UpdateBirdSpeciesRequest request) {
        return ResponseEntity.ok(speciesService.update(id, request));
    }

    @PostMapping("/{id}/media")
    public ResponseEntity<BirdMediaResponse> uploadMedia(
            @PathVariable UUID id,
            @RequestParam String assetType,
            @RequestPart MultipartFile file,
            @RequestParam(required = false) String caption,
            @RequestParam(required = false) String attribution) {
        BirdMediaResponse response = mediaService.uploadMedia(id, assetType, file, caption, attribution);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{birdId}/media/{mediaId}")
    public ResponseEntity<Void> deleteMedia(@PathVariable UUID birdId, @PathVariable UUID mediaId) {
        mediaService.deleteMedia(birdId, mediaId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        speciesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}