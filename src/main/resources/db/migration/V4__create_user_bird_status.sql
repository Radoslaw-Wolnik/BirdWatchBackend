CREATE TABLE user_bird_status (
                                  id UUID PRIMARY KEY,
                                  user_id UUID NOT NULL REFERENCES users(id),
                                  bird_species_id UUID NOT NULL REFERENCES bird_species(id),
                                  seen BOOLEAN NOT NULL DEFAULT FALSE,
                                  seen_manually BOOLEAN NOT NULL DEFAULT FALSE,
                                  first_seen_at TIMESTAMP WITH TIME ZONE,
                                  last_seen_at TIMESTAMP WITH TIME ZONE,
                                  seen_count INT NOT NULL DEFAULT 0,
                                  last_seen_location geometry(Point, 4326),
                                  hidden_from_unseen BOOLEAN NOT NULL DEFAULT FALSE,   -- user can hide a bird from 'unseen' list
                                  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
                                  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
                                  UNIQUE (user_id, bird_species_id)
);

CREATE INDEX idx_ubs_user ON user_bird_status(user_id);
CREATE INDEX idx_ubs_user_seen ON user_bird_status(user_id, seen);