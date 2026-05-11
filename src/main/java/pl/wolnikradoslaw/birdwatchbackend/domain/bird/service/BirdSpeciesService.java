package pl.wolnikradoslaw.birdwatchbackend.domain.bird.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.dto.BirdSpeciesResponse;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.dto.CreateBirdSpeciesRequest;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.dto.UpdateBirdSpeciesRequest;

import java.util.UUID;

public interface BirdSpeciesService {
    BirdSpeciesResponse create(CreateBirdSpeciesRequest request);
    BirdSpeciesResponse update(UUID id, UpdateBirdSpeciesRequest request);
    BirdSpeciesResponse getBySlug(String slug);
    Page<BirdSpeciesResponse> list(Pageable pageable);
    void delete(UUID id);
}