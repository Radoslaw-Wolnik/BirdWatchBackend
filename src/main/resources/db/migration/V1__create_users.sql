CREATE EXTENSION IF NOT EXISTS postgis;

CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       role VARCHAR(20) NOT NULL DEFAULT 'USER',
                       profile_picture_object_key VARCHAR(500),
    -- home location stored as PostGIS geometry point (WGS84)
                       home_location geometry(Point, 4326),
                       home_location_label VARCHAR(255),
                       country VARCHAR(100),
                       region VARCHAR(100),
                       timezone VARCHAR(50),
                       favorite_bird_id UUID,          -- will add FK later when bird_species exists
                       active BOOLEAN NOT NULL DEFAULT TRUE,
                       created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
                       updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);