package pl.wolnikradoslaw.birdwatchbackend.domain.trip;

import pl.wolnikradoslaw.birdwatchbackend.common.BaseEntity;
import pl.wolnikradoslaw.birdwatchbackend.domain.trip.enums.TripStatus;
import pl.wolnikradoslaw.birdwatchbackend.domain.user.User;
import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "birding_trips")
public class BirdingTrip extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TripStatus status = TripStatus.DRAFT;

    private Instant startedAt;
    private Instant endedAt;

    @Column(columnDefinition = "geometry(Point,4326)")
    private Point startLocation;

    @Column(columnDefinition = "geometry(Point,4326)")
    private Point endLocation;

    private Double distanceMeters;
    private Long durationSeconds;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BirdObservation> observations = new ArrayList<>();

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public TripStatus getStatus() { return status; }
    public void setStatus(TripStatus status) { this.status = status; }

    public Instant getStartedAt() { return startedAt; }
    public void setStartedAt(Instant startedAt) { this.startedAt = startedAt; }

    public Instant getEndedAt() { return endedAt; }
    public void setEndedAt(Instant endedAt) { this.endedAt = endedAt; }

    public Point getStartLocation() { return startLocation; }
    public void setStartLocation(Point startLocation) { this.startLocation = startLocation; }

    public Point getEndLocation() { return endLocation; }
    public void setEndLocation(Point endLocation) { this.endLocation = endLocation; }

    public Double getDistanceMeters() { return distanceMeters; }
    public void setDistanceMeters(Double distanceMeters) { this.distanceMeters = distanceMeters; }

    public Long getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(Long durationSeconds) { this.durationSeconds = durationSeconds; }

    public List<BirdObservation> getObservations() { return observations; }
    public void setObservations(List<BirdObservation> observations) { this.observations = observations; }
}