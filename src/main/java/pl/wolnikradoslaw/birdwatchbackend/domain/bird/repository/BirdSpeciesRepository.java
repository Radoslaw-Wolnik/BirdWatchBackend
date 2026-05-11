package pl.wolnikradoslaw.birdwatchbackend.domain.bird.repository;

import pl.wolnikradoslaw.birdwatchbackend.domain.bird.BirdSpecies;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface BirdSpeciesRepository extends JpaRepository<BirdSpecies, UUID> {
    Optional<BirdSpecies> findBySlug(String slug);
    boolean existsBySlug(String slug);
}