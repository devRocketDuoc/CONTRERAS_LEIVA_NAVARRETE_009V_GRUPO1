CREATE TABLE IF NOT EXISTS vehicle (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(100) NOT NULL UNIQUE,
    model VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    battery_percentage INT NOT NULL,
    terminal_id BIGINT,
    charging_station_id BIGINT,
    active BOOLEAN NOT NULL DEFAULT TRUE
);
