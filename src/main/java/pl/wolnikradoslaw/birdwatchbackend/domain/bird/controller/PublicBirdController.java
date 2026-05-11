package pl.wolnikradoslaw.birdwatchbackend.domain.bird.controller;

import pl.wolnikradoslaw.birdwatchbackend.domain.bird.dto.BirdMediaResponse;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.dto.BirdSpeciesResponse;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.service.BirdSpeciesService;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.service.MediaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/birds")
public class PublicBirdController {

    private final BirdSpeciesService speciesService;
    private final MediaService mediaService;

    public PublicBirdController(BirdSpeciesService speciesService, MediaService mediaService) {
        this.speciesService = speciesService;
        this.mediaService = mediaService;
    }

    @GetMapping
    public Page<BirdSpeciesResponse> listBirds(Pageable pageable) {
        return speciesService.list(pageable);
    }

    @GetMapping("/{slug}")
    public BirdSpeciesResponse getBird(@PathVariable String slug) {
        return speciesService.getBySlug(slug);
    }

    @GetMapping("/{slug}/media")
    public List<BirdMediaResponse> getBirdMedia(@PathVariable String slug) {
        BirdSpeciesResponse bird = speciesService.getBySlug(slug);
        return mediaService.getMediaForBird(bird.id());
    }
}