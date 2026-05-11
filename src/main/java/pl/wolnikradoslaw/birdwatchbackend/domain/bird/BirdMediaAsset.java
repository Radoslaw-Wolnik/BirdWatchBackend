package pl.wolnikradoslaw.birdwatchbackend.domain.bird;

import pl.wolnikradoslaw.birdwatchbackend.common.BaseEntity;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.enums.MediaAssetType;
import jakarta.persistence.*;

@Entity
@Table(name = "bird_media_assets")
public class BirdMediaAsset extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bird_species_id")
    private BirdSpecies birdSpecies;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MediaAssetType assetType;

    @Column(nullable = false, length = 600)
    private String storageKey;

    @Column(nullable = false, length = 100)
    private String contentType;

    @Column(nullable = false, length = 255)
    private String fileName;

    private Long fileSize;
    private Integer width;
    private Integer height;
    private Long durationMs;

    @Column(length = 255)
    private String caption;

    @Column(length = 255)
    private String sourceAttribution;

    private int sortOrder;

    public BirdSpecies getBirdSpecies() { return birdSpecies; }
    public void setBirdSpecies(BirdSpecies birdSpecies) { this.birdSpecies = birdSpecies; }

    public MediaAssetType getAssetType() { return assetType; }
    public void setAssetType(MediaAssetType assetType) { this.assetType = assetType; }

    public String getStorageKey() { return storageKey; }
    public void setStorageKey(String storageKey) { this.storageKey = storageKey; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public Integer getWidth() { return width; }
    public void setWidth(Integer width) { this.width = width; }

    public Integer getHeight() { return height; }
    public void setHeight(Integer height) { this.height = height; }

    public Long getDurationMs() { return durationMs; }
    public void setDurationMs(Long durationMs) { this.durationMs = durationMs; }

    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }

    public String getSourceAttribution() { return sourceAttribution; }
    public void setSourceAttribution(String sourceAttribution) { this.sourceAttribution = sourceAttribution; }

    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
}