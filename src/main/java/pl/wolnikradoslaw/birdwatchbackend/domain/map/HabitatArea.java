package pl.wolnikradoslaw.birdwatchbackend.domain.map;

import pl.wolnikradoslaw.birdwatchbackend.common.BaseEntity;
import pl.wolnikradoslaw.birdwatchbackend.domain.bird.BirdSpecies;
import jakarta.persistence.*;
import org.locationtech.jts.geom.MultiPolygon;

@Entity
@Table(name = "habitat_areas")
public class HabitatArea extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bird_species_id")
    private BirdSpecies birdSpecies;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, columnDefinition = "geometry(MultiPolygon,4326)")
    private MultiPolygon geometry;

    public BirdSpecies getBirdSpecies() { return birdSpecies; }
    public void setBirdSpecies(BirdSpecies birdSpecies) { this.birdSpecies = birdSpecies; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public MultiPolygon getGeometry() { return geometry; }
    public void setGeometry(MultiPolygon geometry) { this.geometry = geometry; }
}