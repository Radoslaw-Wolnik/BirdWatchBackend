CREATE TABLE birding_trips (
                               id UUID PRIMARY KEY,
                               user_id UUID NOT NULL REFERENCES users(id),
                               title VARCHAR(150) NOT NULL,
                               description TEXT,
                               status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
                               started_at TIMESTAMP WITH TIME ZONE,
                               ended_at TIMESTAMP WITH TIME ZONE,
                               start_location geometry(Point, 4326),
                               end_location geometry(Point, 4326),
                               distance_meters DOUBLE PRECISION,
                               duration_seconds BIGINT,
                               created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
                               updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE TABLE bird_observations (
                                   id UUID PRIMARY KEY,
                                   trip_id UUID REFERENCES birding_trips(id) ON DELETE CASCADE,
                                   user_id UUID NOT NULL REFERENCES users(id),
                                   bird_species_id UUID NOT NULL REFERENCES bird_species(id),
                                   observed_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                   location geometry(Point, 4326) NOT NULL,
                                   location_accuracy_meters DOUBLE PRECISION,
                                   note TEXT,
                                   photo_object_key VARCHAR(600),
                                   sound_object_key VARCHAR(600),
                                   manual_seen_mark BOOLEAN NOT NULL DEFAULT FALSE,
                                   public_visible BOOLEAN NOT NULL DEFAULT TRUE,
                                   created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
                                   updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

-- Indexes for common queries
CREATE INDEX idx_trips_user ON birding_trips(user_id);
CREATE INDEX idx_observations_trip ON bird_observations(trip_id);
CREATE INDEX idx_observations_user ON bird_observations(user_id);
CREATE INDEX idx_observations_species ON bird_observations(bird_species_id);
CREATE INDEX idx_observations_location ON bird_observations USING GIST(location);