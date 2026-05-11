package pl.wolnikradoslaw.birdwatchbackend.domain.map.repository;

import com.example.birdwatchbackend.domain.map.entity.HabitatArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface HabitatAreaRepository extends JpaRepository<HabitatArea, UUID> {

    List<HabitatArea> findByBirdSpeciesId(UUID birdSpeciesId);

    // Optional: get all habitats that intersect a bounding box (for large maps)
    @Query(value = """
        SELECT h.* FROM habitat_areas h
        WHERE ST_Intersects(h.geometry, ST_MakeEnvelope(:west, :south, :east, :north, 4326))
    """, nativeQuery = true)
    List<HabitatArea> findByBoundingBox(@Param("west") double west,
                                        @Param("south") double south,
                                        @Param("east") double east,
                                        @Param("north") double north);
}