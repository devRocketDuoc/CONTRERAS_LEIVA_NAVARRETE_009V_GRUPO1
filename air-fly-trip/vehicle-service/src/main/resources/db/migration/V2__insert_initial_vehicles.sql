INSERT INTO vehicle (
    code,
    model,
    status,
    battery_percentage,
    terminal_id,
    charging_station_id,
    active
)
SELECT
    'AFT-VH-001',
    'AeroTaxi VTOL A1',
    'AVAILABLE',
    95,
    1,
    1,
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM vehicle WHERE code = 'AFT-VH-001'
);

INSERT INTO vehicle (
    code,
    model,
    status,
    battery_percentage,
    terminal_id,
    charging_station_id,
    active
)
SELECT
    'AFT-VH-002',
    'AeroTaxi VTOL A1',
    'AVAILABLE',
    88,
    2,
    2,
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM vehicle WHERE code = 'AFT-VH-002'
);

INSERT INTO vehicle (
    code,
    model,
    status,
    battery_percentage,
    terminal_id,
    charging_station_id,
    active
)
SELECT
    'AFT-VH-003',
    'AeroTaxi VTOL B2',
    'CHARGING',
    45,
    3,
    3,
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM vehicle WHERE code = 'AFT-VH-003'
);

INSERT INTO vehicle (
    code,
    model,
    status,
    battery_percentage,
    terminal_id,
    charging_station_id,
    active
)
SELECT
    'AFT-VH-004',
    'AeroTaxi VTOL B2',
    'AVAILABLE',
    76,
    4,
    4,
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM vehicle WHERE code = 'AFT-VH-004'
);

INSERT INTO vehicle (
    code,
    model,
    status,
    battery_percentage,
    terminal_id,
    charging_station_id,
    active
)
SELECT
    'AFT-VH-005',
    'AeroTaxi VTOL C3',
    'MAINTENANCE',
    60,
    5,
    NULL,
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM vehicle WHERE code = 'AFT-VH-005'
);

INSERT INTO vehicle (
    code,
    model,
    status,
    battery_percentage,
    terminal_id,
    charging_station_id,
    active
)
SELECT
    'AFT-VH-006',
    'AeroTaxi VTOL C3',
    'AVAILABLE',
    91,
    6,
    6,
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM vehicle WHERE code = 'AFT-VH-006'
);

INSERT INTO vehicle (
    code,
    model,
    status,
    battery_percentage,
    terminal_id,
    charging_station_id,
    active
)
SELECT
    'AFT-VH-007',
    'AeroTaxi VTOL A1',
    'IN_TRIP',
    67,
    7,
    NULL,
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM vehicle WHERE code = 'AFT-VH-007'
);

INSERT INTO vehicle (
    code,
    model,
    status,
    battery_percentage,
    terminal_id,
    charging_station_id,
    active
)
SELECT
    'AFT-VH-008',
    'AeroTaxi VTOL B2',
    'AVAILABLE',
    83,
    8,
    8,
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM vehicle WHERE code = 'AFT-VH-008'
);