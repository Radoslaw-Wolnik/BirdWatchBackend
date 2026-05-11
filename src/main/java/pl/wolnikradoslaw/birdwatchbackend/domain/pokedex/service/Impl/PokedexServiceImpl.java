package pl.wolnikradoslaw.birdwatchbackend.domain.pokedex.service.Impl;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.BirdSpecies;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.repository.BirdSpeciesRepository;
import pl.wolnikradoslaw.birdwatchbackend.domain.pokedex.UserBirdStatus;
import pl.wolnikradoslaw.birdwatchbackend.domain.pokedex.dto.PokedexEntryResponse;
import pl.wolnikradoslaw.birdwatchbackend.domain.pokedex.mapper.PokedexMapper;
import pl.wolnikradoslaw.birdwatchbackend.domain.pokedex.repository.UserBirdStatusRepository;
import pl.wolnikradoslaw.birdwatchbackend.domain.pokedex.service.PokedexService;
import pl.wolnikradoslaw.birdwatchbackend.domain.user.User;
import pl.wolnikradoslaw.birdwatchbackend.domain.user.repository.UserRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PokedexServiceImpl implements PokedexService {

    private final UserBirdStatusRepository statusRepository;
    private final UserRepository userRepository;
    private final BirdSpeciesRepository birdSpeciesRepository;
    private final PokedexMapper mapper;
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    public PokedexServiceImpl(UserBirdStatusRepository statusRepository,
                              UserRepository userRepository,
                              BirdSpeciesRepository birdSpeciesRepository,
                              PokedexMapper mapper) {
        this.statusRepository = statusRepository;
        this.userRepository = userRepository;
        this.birdSpeciesRepository = birdSpeciesRepository;
        this.mapper = mapper;
    }

    @Override
    public List<PokedexEntryResponse> getUserPokedex(UUID userId, String groupBy) {
        List<UserBirdStatus> entries = statusRepository.findByUserId(userId);
        return entries.stream()
                .map(mapper::toResponse)
                .sorted(getComparator(groupBy))
                .collect(Collectors.toList());
    }

    @Override
    public List<PokedexEntryResponse> getUnseenBirds(UUID userId) {
        List<BirdSpecies> unseenSpecies = statusRepository.findUnseenSpeciesForUser(userId);
        return unseenSpecies.stream()
                .map(mapper::toUnseenResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsSeen(UUID userId, UUID birdSpeciesId, boolean seenManually) {
        UserBirdStatus status = statusRepository.findByUserIdAndBirdSpeciesId(userId, birdSpeciesId)
                .orElseGet(() -> createNewStatus(userId, birdSpeciesId));
        status.setSeen(true);
        status.setSeenManually(seenManually);
        if (status.getFirstSeenAt() == null) {
            status.setFirstSeenAt(Instant.now());
            status.setLastSeenAt(Instant.now());
            status.setSeenCount(1);
        } else if (seenManually) {
            // manual mark doesn't update timestamp/count unless it's the first time
        }
        statusRepository.save(status);
    }

    @Override
    @Transactional
    public void markAsUnseen(UUID userId, UUID birdSpeciesId) {
        statusRepository.findByUserIdAndBirdSpeciesId(userId, birdSpeciesId)
                .ifPresent(status -> {
                    status.setSeen(false);
                    status.setSeenManually(false);
                    statusRepository.save(status);
                });
    }

    @Override
    @Transactional
    public void hideFromUnseen(UUID userId, UUID birdSpeciesId) {
        UserBirdStatus status = statusRepository.findByUserIdAndBirdSpeciesId(userId, birdSpeciesId)
                .orElseGet(() -> createNewStatus(userId, birdSpeciesId));
        status.setHiddenFromUnseen(true);
        statusRepository.save(status);
    }

    @Override
    @Transactional
    public void recordObservation(UUID userId, UUID birdSpeciesId, Double latitude, Double longitude, Instant observedAt) {
        UserBirdStatus status = statusRepository.findByUserIdAndBirdSpeciesId(userId, birdSpeciesId)
                .orElseGet(() -> createNewStatus(userId, birdSpeciesId));

        status.setSeen(true);
        // Do not overwrite manual mark if it was already manual? We'll keep it: real observation always sets seenManually to false.
        status.setSeenManually(false);

        if (status.getFirstSeenAt() == null || observedAt.isBefore(status.getFirstSeenAt())) {
            status.setFirstSeenAt(observedAt);
        }
        if (status.getLastSeenAt() == null || observedAt.isAfter(status.getLastSeenAt())) {
            status.setLastSeenAt(observedAt);
        }
        status.setSeenCount(status.getSeenCount() + 1);

        if (latitude != null && longitude != null) {
            Point location = geometryFactory.createPoint(new Coordinate(longitude, latitude));
            status.setLastSeenLocation(location);
        }
        statusRepository.save(status);
    }

    private UserBirdStatus createNewStatus(UUID userId, UUID birdSpeciesId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        BirdSpecies species = birdSpeciesRepository.findById(birdSpeciesId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bird species not found"));
        UserBirdStatus status = new UserBirdStatus();
        status.setUser(user);
        status.setBirdSpecies(species);
        return status;
    }

    private Comparator<PokedexEntryResponse> getComparator(String groupBy) {
        if ("rarity".equalsIgnoreCase(groupBy)) {
            return Comparator.comparing(PokedexEntryResponse::rarity);
        } else if ("habitat".equalsIgnoreCase(groupBy)) {
            return Comparator.comparing(PokedexEntryResponse::habitatDescription, Comparator.nullsLast(String::compareTo));
        } else { // default: alphabetically by common name
            return Comparator.comparing(PokedexEntryResponse::commonName);
        }
    }
}