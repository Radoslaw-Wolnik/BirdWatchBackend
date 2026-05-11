package pl.wolnikradoslaw.birdwatchbackend.domain.trip.mapper;

import pl.wolnikradoslaw.birdwatchbackend.domain.trip.BirdObservation;
import pl.wolnikradoslaw.birdwatchbackend.domain.trip.dto.ObservationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.locationtech.jts.geom.Point;

@Mapper(componentModel = "spring")
public interface ObservationMapper {

    @Mapping(target = "birdCommonName", source = "birdSpecies.commonName")
    @Mapping(target = "latitude", source = "location", qualifiedByName = "extractLat")
    @Mapping(target = "longitude", source = "location", qualifiedByName = "extractLon")
    @Mapping(target = "photoUrl", ignore = true)   // set in service
    @Mapping(target = "soundUrl", ignore = true)   // set in service
    ObservationResponse toResponse(BirdObservation observation);

    @Named("extractLat")
    static Double extractLat(Point point) {
        return point != null ? point.getY() : null;
    }

    @Named("extractLon")
    static Double extractLon(Point point) {
        return point != null ? point.getX() : null;
    }
}