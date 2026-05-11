package pl.wolnikradoslaw.birdwatchbackend.domain.bird.service;

import pl.wolnikradoslaw.birdwatchbackend.domain.bird.dto.BirdMediaResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MediaService {
    BirdMediaResponse uploadMedia(UUID birdSpeciesId, String assetType, MultipartFile file, String caption, String attribution);
    List<BirdMediaResponse> getMediaForBird(UUID birdSpeciesId);
    void deleteMedia(UUID birdSpeciesId, UUID mediaId);
}