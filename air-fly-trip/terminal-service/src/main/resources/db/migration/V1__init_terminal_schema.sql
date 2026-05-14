CREATE TABLE IF NOT EXISTS terminal (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    location_description VARCHAR(255),
    latitude DECIMAL(10,7),
    longitude DECIMAL(10,7),
    active BOOLEAN NOT NULL DEFAULT TRUE
);
