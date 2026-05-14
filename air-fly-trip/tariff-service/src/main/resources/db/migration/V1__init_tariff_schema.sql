CREATE TABLE IF NOT EXISTS tariff (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    route_id BIGINT NOT NULL,
    base_price DECIMAL(10,2) NOT NULL,
    price_per_km DECIMAL(10,2) NOT NULL,
    vehicle_type VARCHAR(100),
    active BOOLEAN NOT NULL DEFAULT TRUE
);
