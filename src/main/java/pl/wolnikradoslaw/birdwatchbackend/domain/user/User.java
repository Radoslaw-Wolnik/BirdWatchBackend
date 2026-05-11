package pl.wolnikradoslaw.birdwatchbackend.domain.user;

import pl.wolnikradoslaw.birdwatchbackend.common.BaseEntity;
import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role = UserRole.USER;

    @Column(name = "profile_picture_object_key", length = 500)
    private String profilePictureObjectKey;

    @Column(columnDefinition = "geometry(Point,4326)")
    private Point homeLocation;

    @Column(name = "home_location_label", length = 255)
    private String homeLocationLabel;

    @Column(length = 100)
    private String country;

    @Column(length = 100)
    private String region;

    @Column(length = 50)
    private String timezone;

    // When BirdSpecies is built, we'll add a ManyToOne here:
    // @ManyToOne(fetch = FetchType.LAZY)
    // private BirdSpecies favoriteBird;

    @Column(nullable = false)
    private boolean active = true;

    // Getters and setters (generate with Alt+Insert in IntelliJ or use Lombok later)
    // For now I’ll write them manually for clarity.

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public String getProfilePictureObjectKey() { return profilePictureObjectKey; }
    public void setProfilePictureObjectKey(String key) { this.profilePictureObjectKey = key; }

    public Point getHomeLocation() { return homeLocation; }
    public void setHomeLocation(Point homeLocation) { this.homeLocation = homeLocation; }

    public String getHomeLocationLabel() { return homeLocationLabel; }
    public void setHomeLocationLabel(String label) { this.homeLocationLabel = label; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}