package pl.wolnikradoslaw.birdwatchbackend.domain.trip;

import pl.wolnikradoslaw.birdwatchbackend.common.BaseEntity;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.BirdSpecies;
import pl.wolnikradoslaw.birdwatchbackend.domain.user.User;
import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;

import java.time.Instant;

@Entity
@Table(name = "bird_observations")
public class BirdObservation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private BirdingTrip trip;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bird_species_id")
    private BirdSpecies birdSpecies;

    @Column(nullable = false)
    private Instant observedAt;

    @Column(nullable = false, columnDefinition = "geometry(Point,4326)")
    private Point location;

    private Double locationAccuracyMeters;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(length = 600)
    private String photoObjectKey;

    @Column(length = 600)
    private String soundObjectKey;

    private boolean manualSeenMark;
    private boolean publicVisible = true;

    public BirdingTrip getTrip() { return trip; }
    public void setTrip(BirdingTrip trip) { this.trip = trip; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public BirdSpecies getBirdSpecies() { return birdSpecies; }
    public void setBirdSpecies(BirdSpecies birdSpecies) { this.birdSpecies = birdSpecies; }

    public Instant getObservedAt() { return observedAt; }
    public void setObservedAt(Instant observedAt) { this.observedAt = observedAt; }

    public Point getLocation() { return location; }
    public void setLocation(Point location) { this.location = location; }

    public Double getLocationAccuracyMeters() { return locationAccuracyMeters; }
    public void setLocationAccuracyMeters(Double locationAccuracyMeters) { this.locationAccuracyMeters = locationAccuracyMeters; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getPhotoObjectKey() { return photoObjectKey; }
    public void setPhotoObjectKey(String photoObjectKey) { this.photoObjectKey = photoObjectKey; }

    public String getSoundObjectKey() { return soundObjectKey; }
    public void setSoundObjectKey(String soundObjectKey) { this.soundObjectKey = soundObjectKey; }

    public boolean isManualSeenMark() { return manualSeenMark; }
    public void setManualSeenMark(boolean manualSeenMark) { this.manualSeenMark = manualSeenMark; }

    public boolean isPublicVisible() { return publicVisible; }
    public void setPublicVisible(boolean publicVisible) { this.publicVisible = publicVisible; }
}