INSERT INTO terminal (
    name,
    city,
    location_description,
    latitude,
    longitude,
    active
)
SELECT
    'Terminal Baquedano',
    'Santiago',
    'Referencia basada en estación Metro Baquedano, conexión Línea 1 y Línea 5',
    -33.4372000,
    -70.6345000,
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM terminal WHERE name = 'Terminal Baquedano'
);

INSERT INTO terminal (
    name,
    city,
    location_description,
    latitude,
    longitude,
    active
)
SELECT
    'Terminal Universidad de Chile',
    'Santiago',
    'Referencia basada en estación Metro Universidad de Chile, conexión Línea 1 y Línea 3',
    -33.4430000,
    -70.6506000,
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM terminal WHERE name = 'Terminal Universidad de Chile'
);

INSERT INTO terminal (
    name,
    city,
    location_description,
    latitude,
    longitude,
    active
)
SELECT
    'Terminal La Moneda',
    'Santiago',
    'Referencia basada en estación Metro La Moneda, sector centro cívico',
    -33.4445000,
    -70.6547000,
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM terminal WHERE name = 'Terminal La Moneda'
);

INSERT INTO terminal (
    name,
    city,
    location_description,
    latitude,
    longitude,
    active
)
SELECT
    'Terminal Los Leones',
    'Providencia',
    'Referencia basada en estación Metro Los Leones, conexión Línea 1 y Línea 6',
    -33.4213000,
    -70.6090000,
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM terminal WHERE name = 'Terminal Los Leones'
);

INSERT INTO terminal (
    name,
    city,
    location_description,
    latitude,
    longitude,
    active
)
SELECT
    'Terminal Tobalaba',
    'Las Condes',
    'Referencia basada en estación Metro Tobalaba, conexión Línea 1 y Línea 4',
    -33.4181000,
    -70.6010000,
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM terminal WHERE name = 'Terminal Tobalaba'
);

INSERT INTO terminal (
    name,
    city,
    location_description,
    latitude,
    longitude,
    active
)
SELECT
    'Terminal Escuela Militar',
    'Las Condes',
    'Referencia basada en estación Metro Escuela Militar, sector oriente de Santiago',
    -33.4139000,
    -70.5848000,
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM terminal WHERE name = 'Terminal Escuela Militar'
);

INSERT INTO terminal (
    name,
    city,
    location_description,
    latitude,
    longitude,
    active
)
SELECT
    'Terminal Estación Central',
    'Estación Central',
    'Referencia basada en estación Metro Estación Central, conexión con transporte interurbano',
    -33.4527000,
    -70.6786000,
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM terminal WHERE name = 'Terminal Estación Central'
);

INSERT INTO terminal (
    name,
    city,
    location_description,
    latitude,
    longitude,
    active
)
SELECT
    'Terminal Pajaritos',
    'Lo Prado',
    'Referencia basada en estación Metro Pajaritos, conexión hacia terminal de buses',
    -33.4563000,
    -70.7253000,
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM terminal WHERE name = 'Terminal Pajaritos'
);

INSERT INTO terminal (
    name,
    city,
    location_description,
    latitude,
    longitude,
    active
)
SELECT
    'Terminal Plaza de Maipú',
    'Maipú',
    'Referencia basada en estación Metro Plaza de Maipú, extremo poniente de Línea 5',
    -33.5110000,
    -70.7579000,
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM terminal WHERE name = 'Terminal Plaza de Maipú'
);

INSERT INTO terminal (
    name,
    city,
    location_description,
    latitude,
    longitude,
    active
)
SELECT
    'Terminal Vespucio Norte',
    'Recoleta',
    'Referencia basada en estación Metro Vespucio Norte, extremo norte de Línea 2',
    -33.3809000,
    -70.6463000,
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM terminal WHERE name = 'Terminal Vespucio Norte'
);

INSERT INTO terminal (
    name,
    city,
    location_description,
    latitude,
    longitude,
    active
)
SELECT
    'Terminal Franklin',
    'Santiago',
    'Referencia basada en estación Metro Franklin, conexión Línea 2 y Línea 6',
    -33.4758000,
    -70.6499000,
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM terminal WHERE name = 'Terminal Franklin'
);

INSERT INTO terminal (
    name,
    city,
    location_description,
    latitude,
    longitude,
    active
)
SELECT
    'Terminal Ñuñoa',
    'Ñuñoa',
    'Referencia basada en estación Metro Ñuñoa, conexión Línea 3 y Línea 6',
    -33.4546000,
    -70.6045000,
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM terminal WHERE name = 'Terminal Ñuñoa'
);
