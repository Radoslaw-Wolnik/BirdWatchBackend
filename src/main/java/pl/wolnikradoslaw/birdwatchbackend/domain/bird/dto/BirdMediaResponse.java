package pl.wolnikradoslaw.birdwatchbackend.domain.bird.dto;

import pl.wolnikradoslaw.birdwatchbackend.domain.bird.enums.MediaAssetType;
import java.util.UUID;

public record BirdMediaResponse(
        UUID id,
        MediaAssetType assetType,
        String fileName,
        String url,
        String contentType,
        Long fileSize,
        Integer width,
        Integer height,
        Long durationMs,
        String caption,
        String sourceAttribution,
        int sortOrder
) {}