INSERT INTO trip (
    user_id,
    vehicle_id,
    origin_terminal_id,
    destination_terminal_id,
    route_id,
    status,
    scheduled_at,
    started_at,
    finished_at,
    active,
    created_at
)
SELECT
    3,
    1,
    1,
    2,
    1,
    'REQUESTED',
    DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 2 HOUR),
    NULL,
    NULL,
    TRUE,
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM trip WHERE user_id = 3 AND route_id = 1 AND status = 'REQUESTED'
);

INSERT INTO trip (
    user_id,
    vehicle_id,
    origin_terminal_id,
    destination_terminal_id,
    route_id,
    status,
    scheduled_at,
    started_at,
    finished_at,
    active,
    created_at
)
SELECT
    3,
    2,
    2,
    5,
    2,
    'CONFIRMED',
    DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 5 HOUR),
    NULL,
    NULL,
    TRUE,
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM trip WHERE user_id = 3 AND route_id = 2 AND status = 'CONFIRMED'
);

INSERT INTO trip (
    user_id,
    vehicle_id,
    origin_terminal_id,
    destination_terminal_id,
    route_id,
    status,
    scheduled_at,
    started_at,
    finished_at,
    active,
    created_at
)
SELECT
    2,
    7,
    7,
    8,
    4,
    'IN_PROGRESS',
    DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 20 MINUTE),
    DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 MINUTE),
    NULL,
    TRUE,
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM trip WHERE user_id = 2 AND route_id = 4 AND status = 'IN_PROGRESS'
);

INSERT INTO trip (
    user_id,
    vehicle_id,
    origin_terminal_id,
    destination_terminal_id,
    route_id,
    status,
    scheduled_at,
    started_at,
    finished_at,
    active,
    created_at
)
SELECT
    3,
    4,
    4,
    10,
    10,
    'FINISHED',
    DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY),
    DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY),
    DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY) + INTERVAL 20 MINUTE,
    FALSE,
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM trip WHERE user_id = 3 AND route_id = 10 AND status = 'FINISHED'
);
