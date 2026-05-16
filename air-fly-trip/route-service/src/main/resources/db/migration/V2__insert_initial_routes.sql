INSERT INTO route (
    origin_terminal_id,
    destination_terminal_id,
    distance_km,
    estimated_minutes,
    active
)
SELECT
    1,
    2,
    2.10,
    5,
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM route WHERE origin_terminal_id = 1 AND destination_terminal_id = 2
);

INSERT INTO route (
    origin_terminal_id,
    destination_terminal_id,
    distance_km,
    estimated_minutes,
    active
)
SELECT
    2,
    5,
    5.30,
    9,
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM route WHERE origin_terminal_id = 2 AND destination_terminal_id = 5
);

INSERT INTO route (
    origin_terminal_id,
    destination_terminal_id,
    distance_km,
    estimated_minutes,
    active
)
SELECT
    5,
    6,
    2.50,
    4,
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM route WHERE origin_terminal_id = 5 AND destination_terminal_id = 6
);

INSERT INTO route (
    origin_terminal_id,
    destination_terminal_id,
    distance_km,
    estimated_minutes,
    active
)
SELECT
    7,
    8,
    5.80,
    10,
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM route WHERE origin_terminal_id = 7 AND destination_terminal_id = 8
);

INSERT INTO route (
    origin_terminal_id,
    destination_terminal_id,
    distance_km,
    estimated_minutes,
    active
)
SELECT
    8,
    9,
    8.20,
    13,
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM route WHERE origin_terminal_id = 8 AND destination_terminal_id = 9
);

INSERT INTO route (
    origin_terminal_id,
    destination_terminal_id,
    distance_km,
    estimated_minutes,
    active
)
SELECT
    10,
    1,
    7.40,
    12,
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM route WHERE origin_terminal_id = 10 AND destination_terminal_id = 1
);

INSERT INTO route (
    origin_terminal_id,
    destination_terminal_id,
    distance_km,
    estimated_minutes,
    active
)
SELECT
    11,
    12,
    4.90,
    8,
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM route WHERE origin_terminal_id = 11 AND destination_terminal_id = 12
);

INSERT INTO route (
    origin_terminal_id,
    destination_terminal_id,
    distance_km,
    estimated_minutes,
    active
)
SELECT
    12,
    4,
    6.10,
    10,
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM route WHERE origin_terminal_id = 12 AND destination_terminal_id = 4
);

INSERT INTO route (
    origin_terminal_id,
    destination_terminal_id,
    distance_km,
    estimated_minutes,
    active
)
SELECT
    3,
    7,
    3.20,
    7,
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM route WHERE origin_terminal_id = 3 AND destination_terminal_id = 7
);

INSERT INTO route (
    origin_terminal_id,
    destination_terminal_id,
    distance_km,
    estimated_minutes,
    active
)
SELECT
    4,
    10,
    9.80,
    15,
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM route WHERE origin_terminal_id = 4 AND destination_terminal_id = 10
);
