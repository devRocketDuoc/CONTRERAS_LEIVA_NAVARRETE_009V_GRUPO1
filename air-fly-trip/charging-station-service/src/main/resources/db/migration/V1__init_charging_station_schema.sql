CREATE TABLE IF NOT EXISTS charging_station (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    terminal_id BIGINT NOT NULL,
    capacity INT NOT NULL,
    available_slots INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);
