package pl.wolnikradoslaw.birdwatchbackend.domain.map.service.Impl;

import pl.wolnikradoslaw.birdwatchbackend.domain.map.dto.HabitatAreaResponse;
import pl.wolnikradoslaw.birdwatchbackend.domain.map.dto.HotspotResponse;
import pl.wolnikradoslaw.birdwatchbackend.domain.map.dto.RecentSightingResponse;
import pl.wolnikradoslaw.birdwatchbackend.domain.map.repository.HabitatAreaRepository;
import pl.wolnikradoslaw.birdwatchbackend.domain.map.repository.MapQueryRepository;
import pl.wolnikradoslaw.birdwatchbackend.domain.map.service.MapService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MapServiceImpl implements MapService {

    private final MapQueryRepository mapQueryRepository;
    private final HabitatAreaRepository habitatAreaRepository;

    public MapServiceImpl(MapQueryRepository mapQueryRepository, HabitatAreaRepository habitatAreaRepository) {
        this.mapQueryRepository = mapQueryRepository;
        this.habitatAreaRepository = habitatAreaRepository;
    }

    @Override
    public List<RecentSightingResponse> getRecentSightings(double north, double south, double east, double west, Instant since) {
        return mapQueryRepository.findRecentObservations(north, south, east, west, since);
    }

    @Override
    public List<HotspotResponse> getHotspots(UUID speciesId, Instant from, Instant to,
                                             double north, double south, double east, double west) {
        double gridSize = 0.05; // could be a request parameter
        return mapQueryRepository.computeHotspots(speciesId, from, to, north, south, east, west, gridSize);
    }

    @Override
    public List<HabitatAreaResponse> getHabitats(UUID birdSpeciesId) {
        return habitatAreaRepository.findByBirdSpeciesId(birdSpeciesId).stream()
                .map(ha -> new HabitatAreaResponse(
                        ha.getId(),
                        ha.getName(),
                        ha.getDescription(),
                        ha.getGeometry().toText()
                ))
                .collect(Collectors.toList());
    }
}
