package pl.wolnikradoslaw.birdwatchbackend.domain.bird.mapper;

import pl.wolnikradoslaw.birdwatchbackend.domain.bird.BirdSpecies;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.dto.BirdSpeciesResponse;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.dto.CreateBirdSpeciesRequest;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.dto.UpdateBirdSpeciesRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BirdSpeciesMapper {

    BirdSpeciesResponse toResponse(BirdSpecies species);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "mediaAssets", ignore = true)
    BirdSpecies toEntity(CreateBirdSpeciesRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "mediaAssets", ignore = true)
    void updateEntity(UpdateBirdSpeciesRequest request, @MappingTarget BirdSpecies species);
}
