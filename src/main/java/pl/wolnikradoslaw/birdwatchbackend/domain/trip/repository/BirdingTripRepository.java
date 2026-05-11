package pl.wolnikradoslaw.birdwatchbackend.domain.trip.repository;

import pl.wolnikradoslaw.birdwatchbackend.domain.trip.BirdingTrip;
import pl.wolnikradoslaw.birdwatchbackend.domain.trip.enums.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BirdingTripRepository extends JpaRepository<BirdingTrip, UUID> {
    List<BirdingTrip> findByUserIdOrderByCreatedAtDesc(UUID userId);
    Optional<BirdingTrip> findByIdAndUserId(UUID tripId, UUID userId);
    Optional<BirdingTrip> findByUserIdAndStatus(UUID userId, TripStatus status);
}