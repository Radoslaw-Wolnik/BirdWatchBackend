CREATE TABLE habitat_areas (
                               id UUID PRIMARY KEY,
                               bird_species_id UUID NOT NULL REFERENCES bird_species(id) ON DELETE CASCADE,
                               name VARCHAR(150) NOT NULL,
                               description TEXT,
                               geometry geometry(MultiPolygon, 4326) NOT NULL,
                               created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
                               updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE INDEX idx_habitat_areas_species ON habitat_areas(bird_species_id);
CREATE INDEX idx_habitat_areas_geometry ON habitat_areas USING GIST(geometry);