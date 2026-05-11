package pl.wolnikradoslaw.birdwatchbackend.domain.trip.mapper;

import pl.wolnikradoslaw.birdwatchbackend.domain.trip.BirdingTrip;
import pl.wolnikradoslaw.birdwatchbackend.domain.trip.dto.TripResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.locationtech.jts.geom.Point;

@Mapper(componentModel = "spring")
public interface TripMapper {

    @Mapping(target = "startLatitude", source = "startLocation", qualifiedByName = "extractLat")
    @Mapping(target = "startLongitude", source = "startLocation", qualifiedByName = "extractLon")
    @Mapping(target = "endLatitude", source = "endLocation", qualifiedByName = "extractLat")
    @Mapping(target = "endLongitude", source = "endLocation", qualifiedByName = "extractLon")
    @Mapping(target = "observations", ignore = true) // set in service
    TripResponse toResponse(BirdingTrip trip);

    @Named("extractLat")
    static Double extractLat(Point point) {
        return point != null ? point.getY() : null;
    }

    @Named("extractLon")
    static Double extractLon(Point point) {
        return point != null ? point.getX() : null;
    }
}