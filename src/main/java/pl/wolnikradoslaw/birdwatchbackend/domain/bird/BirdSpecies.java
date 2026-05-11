package pl.wolnikradoslaw.birdwatchbackend.domain.bird;

import pl.wolnikradoslaw.birdwatchbackend.common.BaseEntity;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.enums.ActivityPattern;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.enums.BirdRarity;
import jakarta.persistence.*;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bird_species")
public class BirdSpecies extends BaseEntity {

    @Column(nullable = false, length = 120)
    private String commonName;

    @Column(nullable = false, length = 180)
    private String scientificName;

    @Column(nullable = false, unique = true, length = 140)
    private String slug;

    @Column(length = 100)
    private String family;

    @Column(name = "order_name", length = 100)
    private String orderName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String behaviorDescription;

    @Column(columnDefinition = "TEXT")
    private String dietDescription;

    @Column(columnDefinition = "TEXT")
    private String habitatDescription;

    @Column(columnDefinition = "TEXT")
    private String migrationDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private BirdRarity rarity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ActivityPattern activityPattern;

    private boolean migratory;

    @ElementCollection
    @CollectionTable(name = "bird_species_best_months", joinColumns = @JoinColumn(name = "species_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "month")
    private List<Month> bestSeenMonths = new ArrayList<>();

    @Column(length = 80)
    private String conservationStatus;

    @Column(length = 120)
    private String audioCallSpeciesName;

    @OneToMany(mappedBy = "birdSpecies", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BirdMediaAsset> mediaAssets = new ArrayList<>();

    public String getCommonName() { return commonName; }
    public void setCommonName(String commonName) { this.commonName = commonName; }

    public String getScientificName() { return scientificName; }
    public void setScientificName(String scientificName) { this.scientificName = scientificName; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getFamily() { return family; }
    public void setFamily(String family) { this.family = family; }

    public String getOrderName() { return orderName; }
    public void setOrderName(String orderName) { this.orderName = orderName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getBehaviorDescription() { return behaviorDescription; }
    public void setBehaviorDescription(String behaviorDescription) { this.behaviorDescription = behaviorDescription; }

    public String getDietDescription() { return dietDescription; }
    public void setDietDescription(String dietDescription) { this.dietDescription = dietDescription; }

    public String getHabitatDescription() { return habitatDescription; }
    public void setHabitatDescription(String habitatDescription) { this.habitatDescription = habitatDescription; }

    public String getMigrationDescription() { return migrationDescription; }
    public void setMigrationDescription(String migrationDescription) { this.migrationDescription = migrationDescription; }

    public BirdRarity getRarity() { return rarity; }
    public void setRarity(BirdRarity rarity) { this.rarity = rarity; }

    public ActivityPattern getActivityPattern() { return activityPattern; }
    public void setActivityPattern(ActivityPattern activityPattern) { this.activityPattern = activityPattern; }

    public boolean isMigratory() { return migratory; }
    public void setMigratory(boolean migratory) { this.migratory = migratory; }

    public List<Month> getBestSeenMonths() { return bestSeenMonths; }
    public void setBestSeenMonths(List<Month> bestSeenMonths) { this.bestSeenMonths = bestSeenMonths; }

    public String getConservationStatus() { return conservationStatus; }
    public void setConservationStatus(String conservationStatus) { this.conservationStatus = conservationStatus; }

    public String getAudioCallSpeciesName() { return audioCallSpeciesName; }
    public void setAudioCallSpeciesName(String audioCallSpeciesName) { this.audioCallSpeciesName = audioCallSpeciesName; }

    public List<BirdMediaAsset> getMediaAssets() { return mediaAssets; }
    public void setMediaAssets(List<BirdMediaAsset> mediaAssets) { this.mediaAssets = mediaAssets; }
}