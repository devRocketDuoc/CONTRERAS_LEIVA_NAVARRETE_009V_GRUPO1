INSERT INTO reservation (
    user_id,
    route_id,
    origin_terminal_id,
    destination_terminal_id,
    reserved_at,
    status,
    active,
    created_at
)
SELECT
    3,
    1,
    1,
    2,
    DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 1 DAY),
    'CONFIRMED',
    TRUE,
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM reservation WHERE user_id = 3 AND route_id = 1 AND status = 'CONFIRMED'
);

INSERT INTO reservation (
    user_id,
    route_id,
    origin_terminal_id,
    destination_terminal_id,
    reserved_at,
    status,
    active,
    created_at
)
SELECT
    3,
    2,
    2,
    5,
    DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 2 DAY),
    'PENDING',
    TRUE,
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM reservation WHERE user_id = 3 AND route_id = 2 AND status = 'PENDING'
);

INSERT INTO reservation (
    user_id,
    route_id,
    origin_terminal_id,
    destination_terminal_id,
    reserved_at,
    status,
    active,
    created_at
)
SELECT
    2,
    4,
    7,
    8,
    DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 3 DAY),
    'CONFIRMED',
    TRUE,
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM reservation WHERE user_id = 2 AND route_id = 4 AND status = 'CONFIRMED'
);

INSERT INTO reservation (
    user_id,
    route_id,
    origin_terminal_id,
    destination_terminal_id,
    reserved_at,
    status,
    active,
    created_at
)
SELECT
    3,
    5,
    8,
    9,
    DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 5 DAY),
    'CANCELLED',
    FALSE,
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM reservation WHERE user_id = 3 AND route_id = 5 AND status = 'CANCELLED'
);