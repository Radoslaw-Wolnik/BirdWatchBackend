package pl.wolnikradoslaw.birdwatchbackend.domain.trip.service.Impl;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.BirdSpecies;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.repository.BirdSpeciesRepository;
import pl.wolnikradoslaw.birdwatchbackend.domain.pokedex.service.PokedexService;
import pl.wolnikradoslaw.birdwatchbackend.domain.trip.BirdObservation;
import pl.wolnikradoslaw.birdwatchbackend.domain.trip.BirdingTrip;
import pl.wolnikradoslaw.birdwatchbackend.domain.trip.dto.*;
import pl.wolnikradoslaw.birdwatchbackend.domain.trip.enums.TripStatus;
import pl.wolnikradoslaw.birdwatchbackend.domain.trip.mapper.ObservationMapper;
import pl.wolnikradoslaw.birdwatchbackend.domain.trip.mapper.TripMapper;
import pl.wolnikradoslaw.birdwatchbackend.domain.trip.repository.BirdObservationRepository;
import pl.wolnikradoslaw.birdwatchbackend.domain.trip.repository.BirdingTripRepository;
import pl.wolnikradoslaw.birdwatchbackend.domain.trip.service.TripService;
import pl.wolnikradoslaw.birdwatchbackend.domain.user.User;
import pl.wolnikradoslaw.birdwatchbackend.domain.user.repository.UserRepository;
import pl.wolnikradoslaw.birdwatchbackend.infrastructure.storage.FileStorageClient;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TripServiceImpl implements TripService {

    private final BirdingTripRepository tripRepository;
    private final BirdObservationRepository observationRepository;
    private final UserRepository userRepository; // maybe just reference
    private final BirdSpeciesRepository birdSpeciesRepository;
    private final TripMapper tripMapper;
    private final ObservationMapper observationMapper;
    private final FileStorageClient fileStorageClient;
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    private final PokedexService pokedexService;

    @Value("${minio.bucket}")
    private String bucket;

    public TripServiceImpl(BirdingTripRepository tripRepository, BirdObservationRepository observationRepository,
                           UserRepository userRepository, BirdSpeciesRepository birdSpeciesRepository,
                           TripMapper tripMapper, ObservationMapper observationMapper,
                           FileStorageClient fileStorageClient, PokedexService pokedexService) {
        this.tripRepository = tripRepository;
        this.observationRepository = observationRepository;
        this.userRepository = userRepository;
        this.birdSpeciesRepository = birdSpeciesRepository;
        this.tripMapper = tripMapper;
        this.observationMapper = observationMapper;
        this.fileStorageClient = fileStorageClient;
        this.pokedexService = pokedexService;
    }

    @Override
    @Transactional
    public TripResponse createTrip(UUID userId, CreateTripRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        BirdingTrip trip = new BirdingTrip();
        trip.setUser(user);
        trip.setTitle(request.title());
        trip.setDescription(request.description());
        trip.setStatus(TripStatus.DRAFT);
        trip = tripRepository.save(trip);
        return tripMapper.toResponse(trip);
    }

    @Override
    @Transactional
    public TripResponse startTrip(UUID userId, UUID tripId, StartTripRequest request) {
        BirdingTrip trip = getTripEntityForUser(userId, tripId);
        if (trip.getStatus() != TripStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT trips can be started");
        }
        // Prevent multiple active trips
        tripRepository.findByUserIdAndStatus(userId, TripStatus.ACTIVE).ifPresent(t -> {
            throw new IllegalStateException("You already have an active trip. End it first.");
        });
        trip.setStatus(TripStatus.ACTIVE);
        trip.setStartedAt(Instant.now());
        if (request.latitude() != null && request.longitude() != null) {
            trip.setStartLocation(geometryFactory.createPoint(new Coordinate(request.longitude(), request.latitude())));
        }
        trip = tripRepository.save(trip);
        return enrichWithObservations(trip);
    }

    @Override
    @Transactional
    public TripResponse addObservation(UUID userId, UUID tripId, AddObservationRequest request) {
        BirdingTrip trip = getTripEntityForUser(userId, tripId);
        if (trip.getStatus() != TripStatus.ACTIVE) {
            throw new IllegalStateException("Observations can only be added to ACTIVE trips");
        }
        BirdSpecies species = birdSpeciesRepository.findById(request.birdSpeciesId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bird Species not found"));
        User user = userRepository.findById(userId).orElseThrow();

        BirdObservation observation = new BirdObservation();
        observation.setTrip(trip);
        observation.setUser(user);
        observation.setBirdSpecies(species);
        observation.setObservedAt(request.observedAt());
        observation.setLocation(geometryFactory.createPoint(new Coordinate(request.longitude(), request.latitude())));
        observation.setLocationAccuracyMeters(request.locationAccuracyMeters());
        observation.setNote(request.note());
        observation.setPhotoObjectKey(request.photoObjectKey());
        observation.setSoundObjectKey(request.soundObjectKey());
        observation.setManualSeenMark(request.manualSeenMark());
        observation.setPublicVisible(true);
        observationRepository.save(observation);

        // add to pokedex
        pokedexService.recordObservation(userId, request.birdSpeciesId(), request.latitude(), request.longitude(), request.observedAt());
        // Refresh trip to include the new observation
        trip = tripRepository.findById(tripId).orElseThrow();
        return enrichWithObservations(trip);
    }

    @Override
    @Transactional
    public TripResponse endTrip(UUID userId, UUID tripId, EndTripRequest request) {
        BirdingTrip trip = getTripEntityForUser(userId, tripId);
        if (trip.getStatus() != TripStatus.ACTIVE) {
            throw new IllegalStateException("Only ACTIVE trips can be completed");
        }
        trip.setStatus(TripStatus.COMPLETED);
        trip.setEndedAt(Instant.now());
        if (request.endLatitude() != null && request.endLongitude() != null) {
            trip.setEndLocation(geometryFactory.createPoint(new Coordinate(request.endLongitude(), request.endLatitude())));
        }
        trip.setDistanceMeters(request.distanceMeters());
        trip.setDurationSeconds(request.durationSeconds());
        if (request.title() != null) trip.setTitle(request.title());
        if (request.description() != null) trip.setDescription(request.description());
        trip = tripRepository.save(trip);
        return enrichWithObservations(trip);
    }

    @Override
    public TripResponse getTrip(UUID userId, UUID tripId) {
        BirdingTrip trip = getTripEntityForUser(userId, tripId);
        return enrichWithObservations(trip);
    }

    @Override
    public List<TripResponse> listUserTrips(UUID userId) {
        return tripRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::enrichWithObservations)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void cancelTrip(UUID userId, UUID tripId) {
        BirdingTrip trip = getTripEntityForUser(userId, tripId);
        if (trip.getStatus() == TripStatus.COMPLETED || trip.getStatus() == TripStatus.CANCELLED) {
            throw new IllegalStateException("Cannot cancel a completed or already cancelled trip");
        }
        trip.setStatus(TripStatus.CANCELLED);
        tripRepository.save(trip);
    }

    private BirdingTrip getTripEntityForUser(UUID userId, UUID tripId) {
        return tripRepository.findByIdAndUserId(tripId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found"));
    }

    private TripResponse enrichWithObservations(BirdingTrip trip) {
        TripResponse response = tripMapper.toResponse(trip);
        List<ObservationResponse> observationResponses = observationRepository.findByTripIdOrderByObservedAtAsc(trip.getId())
                .stream()
                .map(this::toObservationResponseWithUrls)
                .collect(Collectors.toList());
        // Construct a new TripResponse with observations filled
        return new TripResponse(
                response.id(), response.title(), response.description(), response.status(),
                response.startedAt(), response.endedAt(),
                response.startLatitude(), response.startLongitude(),
                response.endLatitude(), response.endLongitude(),
                response.distanceMeters(), response.durationSeconds(),
                observationResponses
        );
    }

    private ObservationResponse toObservationResponseWithUrls(BirdObservation obs) {
        ObservationResponse base = observationMapper.toResponse(obs);
        String photoUrl = obs.getPhotoObjectKey() != null ?
                fileStorageClient.getPresignedUrl(bucket, obs.getPhotoObjectKey()) : null;
        String soundUrl = obs.getSoundObjectKey() != null ?
                fileStorageClient.getPresignedUrl(bucket, obs.getSoundObjectKey()) : null;
        return new ObservationResponse(
                base.id(), base.birdSpeciesId(), base.birdCommonName(),
                base.observedAt(), base.latitude(), base.longitude(),
                base.locationAccuracyMeters(), base.note(),
                photoUrl, soundUrl, base.manualSeenMark(), base.createdAt()
        );
    }
}