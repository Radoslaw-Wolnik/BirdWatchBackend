package pl.wolnikradoslaw.birdwatchbackend.domain.pokedex;

import pl.wolnikradoslaw.birdwatchbackend.common.BaseEntity;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.BirdSpecies;
import pl.wolnikradoslaw.birdwatchbackend.domain.user.User;
import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;

import java.time.Instant;

@Entity
@Table(name = "user_bird_status",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_species",
                columnNames = {"user_id", "bird_species_id"}))
public class UserBirdStatus extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bird_species_id")
    private BirdSpecies birdSpecies;

    private boolean seen;

    @Column(name = "seen_manually")
    private boolean seenManually;

    private Instant firstSeenAt;
    private Instant lastSeenAt;
    private int seenCount;

    @Column(name = "last_seen_location", columnDefinition = "geometry(Point,4326)")
    private Point lastSeenLocation;

    @Column(name = "hidden_from_unseen")
    private boolean hiddenFromUnseen;

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public BirdSpecies getBirdSpecies() { return birdSpecies; }
    public void setBirdSpecies(BirdSpecies birdSpecies) { this.birdSpecies = birdSpecies; }

    public boolean isSeen() { return seen; }
    public void setSeen(boolean seen) { this.seen = seen; }

    public boolean isSeenManually() { return seenManually; }
    public void setSeenManually(boolean seenManually) { this.seenManually = seenManually; }

    public Instant getFirstSeenAt() { return firstSeenAt; }
    public void setFirstSeenAt(Instant firstSeenAt) { this.firstSeenAt = firstSeenAt; }

    public Instant getLastSeenAt() { return lastSeenAt; }
    public void setLastSeenAt(Instant lastSeenAt) { this.lastSeenAt = lastSeenAt; }

    public int getSeenCount() { return seenCount; }
    public void setSeenCount(int seenCount) { this.seenCount = seenCount; }

    public Point getLastSeenLocation() { return lastSeenLocation; }
    public void setLastSeenLocation(Point lastSeenLocation) { this.lastSeenLocation = lastSeenLocation; }

    public boolean isHiddenFromUnseen() { return hiddenFromUnseen; }
    public void setHiddenFromUnseen(boolean hiddenFromUnseen) { this.hiddenFromUnseen = hiddenFromUnseen; }
}