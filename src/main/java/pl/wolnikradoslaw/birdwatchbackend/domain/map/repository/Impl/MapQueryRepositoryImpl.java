package pl.wolnikradoslaw.birdwatchbackend.domain.map.repository.Impl;

import pl.wolnikradoslaw.birdwatchbackend.domain.map.dto.HotspotResponse;
import pl.wolnikradoslaw.birdwatchbackend.domain.map.dto.RecentSightingResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import pl.wolnikradoslaw.birdwatchbackend.domain.map.repository.MapQueryRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class MapQueryRepositoryImpl implements MapQueryRepository {

    @PersistenceContext
    private EntityManager em;

    private static final double DEFAULT_GRID_SIZE = 0.05; // approx 5.5 km at equator

    @Override
    public List<RecentSightingResponse> findRecentObservations(double north, double south, double east, double west, Instant since) {
        String sql = """
            SELECT
                CAST(o.id AS text) as observation_id,
                CAST(o.bird_species_id AS text) as species_id,
                bs.common_name as bird_name,
                u.username,
                o.observed_at,
                ST_Y(o.location) as lat,
                ST_X(o.location) as lon,
                o.note,
                o.photo_object_key
            FROM bird_observations o
            JOIN bird_species bs ON o.bird_species_id = bs.id
            JOIN users u ON o.user_id = u.id
            WHERE o.public_visible = true
              AND o.observed_at >= :since
              AND ST_Within(o.location, ST_MakeEnvelope(:west, :south, :east, :north, 4326))
            ORDER BY o.observed_at DESC
            LIMIT 500
        """;
        Query query = em.createNativeQuery(sql);
        query.setParameter("since", since);
        query.setParameter("west", west);
        query.setParameter("south", south);
        query.setParameter("east", east);
        query.setParameter("north", north);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();
        return rows.stream().map(row -> new RecentSightingResponse(
                (String) row[0],
                (String) row[1],
                (String) row[2],
                (String) row[3],
                ((java.sql.Timestamp) row[4]).toInstant(),
                (Double) row[5],
                (Double) row[6],
                (String) row[7],
                (String) row[8] // photo key; can later generate presigned URL
        )).collect(Collectors.toList());
    }

    @Override
    public List<HotspotResponse> computeHotspots(UUID speciesId, Instant start, Instant end,
                                                 double north, double south, double east, double west,
                                                 double gridSizeDegrees) {
        double gs = gridSizeDegrees > 0 ? gridSizeDegrees : DEFAULT_GRID_SIZE;
        String sql = """
            SELECT
                ST_Y(ST_SnapToGrid(o.location, :grid)) as lat,
                ST_X(ST_SnapToGrid(o.location, :grid)) as lon,
                COUNT(*) as weight,
                CAST(MAX(o.bird_species_id) AS text) as species_id,
                MAX(bs.common_name) as bird_name
            FROM bird_observations o
            JOIN bird_species bs ON o.bird_species_id = bs.id
            WHERE o.public_visible = true
              AND o.observed_at BETWEEN :start AND :end
              AND (:speciesId IS NULL OR o.bird_species_id = :speciesId)
              AND ST_Within(o.location, ST_MakeEnvelope(:west, :south, :east, :north, 4326))
            GROUP BY ST_SnapToGrid(o.location, :grid)
            ORDER BY weight DESC
        """;
        Query query = em.createNativeQuery(sql);
        query.setParameter("start", start);
        query.setParameter("end", end);
        query.setParameter("speciesId", speciesId);
        query.setParameter("west", west);
        query.setParameter("south", south);
        query.setParameter("east", east);
        query.setParameter("north", north);
        query.setParameter("grid", gs);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();
        return rows.stream().map(row -> new HotspotResponse(
                (Double) row[0],
                (Double) row[1],
                ((Number) row[2]).intValue(),
                (String) row[3],
                (String) row[4]
        )).collect(Collectors.toList());
    }
}