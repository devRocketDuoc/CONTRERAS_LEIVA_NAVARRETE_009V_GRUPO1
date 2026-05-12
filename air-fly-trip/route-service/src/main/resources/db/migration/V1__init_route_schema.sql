CREATE TABLE route (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    origin_terminal_id BIGINT NOT NULL,
    destination_terminal_id BIGINT NOT NULL,
    distance_km DECIMAL(10,2) NOT NULL,
    estimated_duration_minutes INT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);
