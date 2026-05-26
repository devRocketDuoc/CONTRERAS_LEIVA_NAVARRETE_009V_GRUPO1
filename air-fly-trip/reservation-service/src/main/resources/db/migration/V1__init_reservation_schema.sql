CREATE TABLE IF NOT EXISTS reservation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    route_id BIGINT NOT NULL,
    origin_terminal_id BIGINT NOT NULL,
    destination_terminal_id BIGINT NOT NULL,
    reserved_at DATETIME NOT NULL,
    status VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);
