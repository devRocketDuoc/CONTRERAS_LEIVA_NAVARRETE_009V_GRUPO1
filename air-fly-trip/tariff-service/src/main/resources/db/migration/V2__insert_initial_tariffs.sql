INSERT INTO tariff (
    route_id,
    base_price,
    price_per_km,
    vehicle_type,
    active
)
SELECT
    1,
    15000.00,
    1800.00,
    'STANDARD',
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM tariff WHERE route_id = 1 AND vehicle_type = 'STANDARD'
);

INSERT INTO tariff (
    route_id,
    base_price,
    price_per_km,
    vehicle_type,
    active
)
SELECT
    1,
    22000.00,
    2500.00,
    'PREMIUM',
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM tariff WHERE route_id = 1 AND vehicle_type = 'PREMIUM'
);

INSERT INTO tariff (
    route_id,
    base_price,
    price_per_km,
    vehicle_type,
    active
)
SELECT
    2,
    18000.00,
    1900.00,
    'STANDARD',
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM tariff WHERE route_id = 2 AND vehicle_type = 'STANDARD'
);

INSERT INTO tariff (
    route_id,
    base_price,
    price_per_km,
    vehicle_type,
    active
)
SELECT
    2,
    26000.00,
    2700.00,
    'PREMIUM',
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM tariff WHERE route_id = 2 AND vehicle_type = 'PREMIUM'
);

INSERT INTO tariff (
    route_id,
    base_price,
    price_per_km,
    vehicle_type,
    active
)
SELECT
    3,
    14000.00,
    1700.00,
    'STANDARD',
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM tariff WHERE route_id = 3 AND vehicle_type = 'STANDARD'
);

INSERT INTO tariff (
    route_id,
    base_price,
    price_per_km,
    vehicle_type,
    active
)
SELECT
    4,
    20000.00,
    2100.00,
    'STANDARD',
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM tariff WHERE route_id = 4 AND vehicle_type = 'STANDARD'
);

INSERT INTO tariff (
    route_id,
    base_price,
    price_per_km,
    vehicle_type,
    active
)
SELECT
    5,
    23000.00,
    2200.00,
    'STANDARD',
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM tariff WHERE route_id = 5 AND vehicle_type = 'STANDARD'
);

INSERT INTO tariff (
    route_id,
    base_price,
    price_per_km,
    vehicle_type,
    active
)
SELECT
    6,
    21000.00,
    2000.00,
    'STANDARD',
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM tariff WHERE route_id = 6 AND vehicle_type = 'STANDARD'
);

INSERT INTO tariff (
    route_id,
    base_price,
    price_per_km,
    vehicle_type,
    active
)
SELECT
    7,
    17500.00,
    1850.00,
    'STANDARD',
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM tariff WHERE route_id = 7 AND vehicle_type = 'STANDARD'
);

INSERT INTO tariff (
    route_id,
    base_price,
    price_per_km,
    vehicle_type,
    active
)
SELECT
    8,
    19500.00,
    2050.00,
    'STANDARD',
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM tariff WHERE route_id = 8 AND vehicle_type = 'STANDARD'
);