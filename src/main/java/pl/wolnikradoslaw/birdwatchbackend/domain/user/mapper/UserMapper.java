package pl.wolnikradoslaw.birdwatchbackend.domain.user.mapper;

import pl.wolnikradoslaw.birdwatchbackend.domain.user.User;
import pl.wolnikradoslaw.birdwatchbackend.domain.user.dto.UpdateProfileRequest;
import pl.wolnikradoslaw.birdwatchbackend.domain.user.dto.UserProfileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.locationtech.jts.geom.Point;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "homeLatitude", source = "homeLocation", qualifiedByName = "extractLat")
    @Mapping(target = "homeLongitude", source = "homeLocation", qualifiedByName = "extractLon")
    @Mapping(target = "profilePictureUrl", ignore = true)
    UserProfileResponse toProfileResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "homeLocation", ignore = true)
    void updateUserFromRequest(UpdateProfileRequest request, @MappingTarget User user);

    @Named("extractLat")
    static Double extractLat(Point point) {
        return point != null ? point.getY() : null;
    }

    @Named("extractLon")
    static Double extractLon(Point point) {
        return point != null ? point.getX() : null;
    }
}