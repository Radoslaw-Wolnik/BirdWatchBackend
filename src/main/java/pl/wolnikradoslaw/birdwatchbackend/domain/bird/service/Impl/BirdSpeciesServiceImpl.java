package pl.wolnikradoslaw.birdwatchbackend.domain.bird.service.Impl;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.BirdSpecies;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.dto.BirdSpeciesResponse;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.dto.CreateBirdSpeciesRequest;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.dto.UpdateBirdSpeciesRequest;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.mapper.BirdSpeciesMapper;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.repository.BirdSpeciesRepository;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.service.BirdSpeciesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class BirdSpeciesServiceImpl implements BirdSpeciesService {

    private final BirdSpeciesRepository repository;
    private final BirdSpeciesMapper mapper;

    public BirdSpeciesServiceImpl(BirdSpeciesRepository repository, BirdSpeciesMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public BirdSpeciesResponse create(CreateBirdSpeciesRequest request) {
        if (repository.existsBySlug(request.slug())) {
            throw new IllegalArgumentException("A bird with this slug already exists");
        }
        BirdSpecies entity = mapper.toEntity(request);
        BirdSpecies saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public BirdSpeciesResponse update(UUID id, UpdateBirdSpeciesRequest request) {
        BirdSpecies entity = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bird Species Not Found"));
        if (request.slug() != null && !request.slug().equals(entity.getSlug()) && repository.existsBySlug(request.slug())) {
            throw new IllegalArgumentException("Slug already in use");
        }
        mapper.updateEntity(request, entity);
        BirdSpecies saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    public BirdSpeciesResponse getBySlug(String slug) {
        BirdSpecies entity = repository.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bird Species Not Found"));
        return mapper.toResponse(entity);
    }

    @Override
    public Page<BirdSpeciesResponse> list(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bird Species Not Found");
        }
        repository.deleteById(id);
    }
}