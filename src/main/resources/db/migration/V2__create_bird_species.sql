CREATE TABLE bird_species (
                              id UUID PRIMARY KEY,
                              slug VARCHAR(140) NOT NULL UNIQUE,
                              common_name VARCHAR(120) NOT NULL,
                              scientific_name VARCHAR(180) NOT NULL,
                              family VARCHAR(100),
                              order_name VARCHAR(100),
                              description TEXT,
                              behavior_description TEXT,
                              diet_description TEXT,
                              habitat_description TEXT,
                              migration_description TEXT,
                              rarity VARCHAR(30) NOT NULL,
                              activity_pattern VARCHAR(30) NOT NULL,
                              migratory BOOLEAN NOT NULL DEFAULT FALSE,
                              best_seen_months TEXT[],   -- array of month names, e.g. {'JANUARY','FEBRUARY'}
                              conservation_status VARCHAR(80),
                              audio_call_species_name VARCHAR(120),
                              created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
                              updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
                              created_by_admin_user_id UUID            -- FK to users later
);

CREATE TABLE bird_media_assets (
                                   id UUID PRIMARY KEY,
                                   bird_species_id UUID NOT NULL REFERENCES bird_species(id) ON DELETE CASCADE,
                                   asset_type VARCHAR(30) NOT NULL,
                                   storage_key VARCHAR(600) NOT NULL,
                                   content_type VARCHAR(100) NOT NULL,
                                   file_name VARCHAR(255) NOT NULL,
                                   file_size BIGINT,
                                   width INT,
                                   height INT,
                                   duration_ms BIGINT,
                                   caption VARCHAR(255),
                                   source_attribution VARCHAR(255),
                                   sort_order INT NOT NULL DEFAULT 0,
                                   created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
                                   updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

-- Indexes
CREATE INDEX idx_bird_species_slug ON bird_species(slug);
CREATE INDEX idx_media_asset_species ON bird_media_assets(bird_species_id);