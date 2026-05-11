package pl.wolnikradoslaw.birdwatchbackend.domain.pokedex.repository;

import pl.wolnikradoslaw.birdwatchbackend.domain.pokedex.UserBirdStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserBirdStatusRepository extends JpaRepository<UserBirdStatus, UUID> {

    Optional<UserBirdStatus> findByUserIdAndBirdSpeciesId(UUID userId, UUID birdSpeciesId);

    List<UserBirdStatus> findByUserId(UUID userId);

    // All species not yet seen by the user (including those never recorded)
    @Query("""
        SELECT bs FROM BirdSpecies bs
        WHERE bs.id NOT IN (
            SELECT ubs.birdSpecies.id FROM UserBirdStatus ubs WHERE ubs.user.id = :userId AND ubs.seen = true
        )
        AND bs.id NOT IN (
            SELECT ubs.birdSpecies.id FROM UserBirdStatus ubs
            WHERE ubs.user.id = :userId AND ubs.hiddenFromUnseen = true
        )
    """)
    List<com.birdwatchbackend.domain.bird.BirdSpecies> findUnseenSpeciesForUser(@Param("userId") UUID userId);

    // For checking if an unseen entry exists and is hidden
    Optional<UserBirdStatus> findByUserIdAndBirdSpeciesIdAndHiddenFromUnseenTrue(UUID userId, UUID birdSpeciesId);
}