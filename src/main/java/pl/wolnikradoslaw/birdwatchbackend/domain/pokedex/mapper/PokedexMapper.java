package pl.wolnikradoslaw.birdwatchbackend.domain.pokedex.mapper;

import pl.wolnikradoslaw.birdwatchbackend.domain.bird.BirdSpecies;
import pl.wolnikradoslaw.birdwatchbackend.domain.pokedex.UserBirdStatus;
import pl.wolnikradoslaw.birdwatchbackend.domain.pokedex.dto.PokedexEntryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.locationtech.jts.geom.Point;

@Mapper(componentModel = "spring")
public interface PokedexMapper {

    @Mapping(target = "birdSpeciesId", source = "birdSpecies.id")
    @Mapping(target = "commonName", source = "birdSpecies.commonName")
    @Mapping(target = "scientificName", source = "birdSpecies.scientificName")
    @Mapping(target = "slug", source = "birdSpecies.slug")
    @Mapping(target = "rarity", source = "birdSpecies.rarity")
    @Mapping(target = "habitatDescription", source = "birdSpecies.habitatDescription")
    @Mapping(target = "lastSeenLatitude", source = "lastSeenLocation", qualifiedByName = "extractLat")
    @Mapping(target = "lastSeenLongitude", source = "lastSeenLocation", qualifiedByName = "extractLon")
    PokedexEntryResponse toResponse(UserBirdStatus status);

    // Map a BirdSpecies that is unseen (no UserBirdStatus row)
    @Mapping(target = "birdSpeciesId", source = "id")
    @Mapping(target = "commonName", source = "commonName")
    @Mapping(target = "scientificName", source = "scientificName")
    @Mapping(target = "slug", source = "slug")
    @Mapping(target = "rarity", source = "rarity")
    @Mapping(target = "habitatDescription", source = "habitatDescription")
    @Mapping(target = "seen", constant = "false")
    @Mapping(target = "seenManually", constant = "false")
    @Mapping(target = "seenCount", constant = "0")
    @Mapping(target = "firstSeenAt", ignore = true)
    @Mapping(target = "lastSeenAt", ignore = true)
    @Mapping(target = "lastSeenLatitude", ignore = true)
    @Mapping(target = "lastSeenLongitude", ignore = true)
    PokedexEntryResponse toUnseenResponse(BirdSpecies species);

    @Named("extractLat")
    static Double extractLat(Point point) {
        return point != null ? point.getY() : null;
    }

    @Named("extractLon")
    static Double extractLon(Point point) {
        return point != null ? point.getX() : null;
    }
}