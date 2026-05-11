package pl.wolnikradoslaw.birdwatchbackend.domain.bird.service.Impl;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.BirdMediaAsset;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.BirdSpecies;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.dto.BirdMediaResponse;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.enums.MediaAssetType;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.mapper.BirdMediaMapper;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.repository.BirdMediaAssetRepository;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.repository.BirdSpeciesRepository;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.service.MediaService;
import pl.wolnikradoslaw.birdwatchbackend.infrastructure.storage.FileStorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MediaServiceImpl implements MediaService {

    private final BirdSpeciesRepository birdSpeciesRepository;
    private final BirdMediaAssetRepository mediaAssetRepository;
    private final BirdMediaMapper mediaMapper;
    private final FileStorageClient fileStorageClient;

    @Value("${minio.bucket}")
    private String bucket;

    public MediaServiceImpl(BirdSpeciesRepository birdSpeciesRepository,
                            BirdMediaAssetRepository mediaAssetRepository,
                            BirdMediaMapper mediaMapper,
                            FileStorageClient fileStorageClient) {
        this.birdSpeciesRepository = birdSpeciesRepository;
        this.mediaAssetRepository = mediaAssetRepository;
        this.mediaMapper = mediaMapper;
        this.fileStorageClient = fileStorageClient;
    }

    @Transactional
    @Override
    public BirdMediaResponse uploadMedia(UUID birdSpeciesId, String assetType, MultipartFile file,
                                         String caption, String attribution) {
        BirdSpecies species = birdSpeciesRepository.findById(birdSpeciesId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bird species not found"));
        MediaAssetType type = MediaAssetType.valueOf(assetType.toUpperCase());

        String originalFilename = file.getOriginalFilename();
        String objectKey = "birds/" + birdSpeciesId + "/" + UUID.randomUUID() + "_" + originalFilename;

        try {
            fileStorageClient.uploadFile(bucket, objectKey, file.getInputStream(), file.getSize(), file.getContentType());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }

        BirdMediaAsset asset = new BirdMediaAsset();
        asset.setBirdSpecies(species);
        asset.setAssetType(type);
        asset.setStorageKey(objectKey);
        asset.setContentType(file.getContentType());
        asset.setFileName(originalFilename);
        asset.setFileSize(file.getSize());
        if (file.getContentType() != null && file.getContentType().startsWith("image/")) {
            // Optionally extract dimensions with ImageIO – skipped for brevity
        }
        asset.setCaption(caption);
        asset.setSourceAttribution(attribution);
        asset.setSortOrder(mediaAssetRepository.findByBirdSpeciesIdOrderBySortOrderAsc(birdSpeciesId).size());

        BirdMediaAsset saved = mediaAssetRepository.save(asset);
        return toResponseWithUrl(saved);
    }

    @Override
    public List<BirdMediaResponse> getMediaForBird(UUID birdSpeciesId) {
        return mediaAssetRepository.findByBirdSpeciesIdOrderBySortOrderAsc(birdSpeciesId).stream()
                .map(this::toResponseWithUrl)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteMedia(UUID birdSpeciesId, UUID mediaId) {
        BirdMediaAsset asset = mediaAssetRepository.findById(mediaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Media not found"));
        if (!asset.getBirdSpecies().getId().equals(birdSpeciesId)) {
            throw new IllegalArgumentException("Media does not belong to this bird species");
        }
        fileStorageClient.deleteFile(bucket, asset.getStorageKey());
        mediaAssetRepository.delete(asset);
    }

    private BirdMediaResponse toResponseWithUrl(BirdMediaAsset asset) {
        BirdMediaResponse response = mediaMapper.toResponse(asset);
        String url = fileStorageClient.getPresignedUrl(bucket, asset.getStorageKey());
        return new BirdMediaResponse(
                response.id(), response.assetType(), response.fileName(), url,
                response.contentType(), response.fileSize(), response.width(), response.height(),
                response.durationMs(), response.caption(), response.sourceAttribution(), response.sortOrder()
        );
    }
}