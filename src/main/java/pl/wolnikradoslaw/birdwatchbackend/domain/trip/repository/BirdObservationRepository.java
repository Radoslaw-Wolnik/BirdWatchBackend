package pl.wolnikradoslaw.birdwatchbackend.domain.trip.repository;

import pl.wolnikradoslaw.birdwatchbackend.domain.trip.BirdObservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface BirdObservationRepository extends JpaRepository<BirdObservation, UUID> {
    List<BirdObservation> findByTripIdOrderByObservedAtAsc(UUID tripId);
    List<BirdObservation> findByUserIdAndBirdSpeciesId(UUID userId, UUID birdSpeciesId);
    List<BirdObservation> findByUserIdAndObservedAtBetween(UUID userId, Instant start, Instant end);
    // For map features we'll add spatial queries later
}