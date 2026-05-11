package pl.wolnikradoslaw.birdwatchbackend.domain.bird.repository;

import pl.wolnikradoslaw.birdwatchbackend.domain.bird.BirdMediaAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface BirdMediaAssetRepository extends JpaRepository<BirdMediaAsset, UUID> {
    List<BirdMediaAsset> findByBirdSpeciesIdOrderBySortOrderAsc(UUID birdSpeciesId);
    void deleteByBirdSpeciesIdAndId(UUID birdSpeciesId, UUID assetId);
}