INSERT INTO charging_station (
    code,
    name,
    terminal_id,
    capacity,
    available_slots,
    status,
    active
)
SELECT
    'CS-BAQ-01',
    'Estación de Carga Baquedano 01',
    1,
    6,
    6,
    'AVAILABLE',
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM charging_station WHERE code = 'CS-BAQ-01'
);

INSERT INTO charging_station (
    code,
    name,
    terminal_id,
    capacity,
    available_slots,
    status,
    active
)
SELECT
    'CS-UCH-01',
    'Estación de Carga Universidad de Chile 01',
    2,
    5,
    5,
    'AVAILABLE',
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM charging_station WHERE code = 'CS-UCH-01'
);

INSERT INTO charging_station (
    code,
    name,
    terminal_id,
    capacity,
    available_slots,
    status,
    active
)
SELECT
    'CS-LMO-01',
    'Estación de Carga La Moneda 01',
    3,
    4,
    4,
    'AVAILABLE',
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM charging_station WHERE code = 'CS-LMO-01'
);

INSERT INTO charging_station (
    code,
    name,
    terminal_id,
    capacity,
    available_slots,
    status,
    active
)
SELECT
    'CS-LLE-01',
    'Estación de Carga Los Leones 01',
    4,
    8,
    8,
    'AVAILABLE',
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM charging_station WHERE code = 'CS-LLE-01'
);

INSERT INTO charging_station (
    code,
    name,
    terminal_id,
    capacity,
    available_slots,
    status,
    active
)
SELECT
    'CS-TOB-01',
    'Estación de Carga Tobalaba 01',
    5,
    8,
    6,
    'AVAILABLE',
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM charging_station WHERE code = 'CS-TOB-01'
);

INSERT INTO charging_station (
    code,
    name,
    terminal_id,
    capacity,
    available_slots,
    status,
    active
)
SELECT
    'CS-EMI-01',
    'Estación de Carga Escuela Militar 01',
    6,
    6,
    4,
    'AVAILABLE',
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM charging_station WHERE code = 'CS-EMI-01'
);

INSERT INTO charging_station (
    code,
    name,
    terminal_id,
    capacity,
    available_slots,
    status,
    active
)
SELECT
    'CS-ECE-01',
    'Estación de Carga Estación Central 01',
    7,
    7,
    7,
    'AVAILABLE',
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM charging_station WHERE code = 'CS-ECE-01'
);

INSERT INTO charging_station (
    code,
    name,
    terminal_id,
    capacity,
    available_slots,
    status,
    active
)
SELECT
    'CS-PAJ-01',
    'Estación de Carga Pajaritos 01',
    8,
    5,
    3,
    'MAINTENANCE',
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM charging_station WHERE code = 'CS-PAJ-01'
);

INSERT INTO charging_station (
    code,
    name,
    terminal_id,
    capacity,
    available_slots,
    status,
    active
)
SELECT
    'CS-PMA-01',
    'Estación de Carga Plaza de Maipú 01',
    9,
    6,
    6,
    'AVAILABLE',
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM charging_station WHERE code = 'CS-PMA-01'
);

INSERT INTO charging_station (
    code,
    name,
    terminal_id,
    capacity,
    available_slots,
    status,
    active
)
SELECT
    'CS-VNO-01',
    'Estación de Carga Vespucio Norte 01',
    10,
    6,
    5,
    'AVAILABLE',
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM charging_station WHERE code = 'CS-VNO-01'
);
