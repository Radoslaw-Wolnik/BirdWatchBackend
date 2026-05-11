package pl.wolnikradoslaw.birdwatchbackend.domain.bird.mapper;

import pl.wolnikradoslaw.birdwatchbackend.domain.bird.BirdMediaAsset;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.dto.BirdMediaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BirdMediaMapper {

    @Mapping(target = "url", ignore = true) // set in service
    BirdMediaResponse toResponse(BirdMediaAsset asset);
}