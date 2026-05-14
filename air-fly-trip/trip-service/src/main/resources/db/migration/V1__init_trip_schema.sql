CREATE TABLE IF NOT EXISTS trip (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    vehicle_id BIGINT,
    origin_terminal_id BIGINT NOT NULL,
    destination_terminal_id BIGINT NOT NULL,
    route_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    scheduled_at DATETIME,
    started_at DATETIME,
    finished_at DATETIME
);
