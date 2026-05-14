INSERT INTO app_user (
email,
    password_hash,
    first_name,
    last_name,
    document_number,
    phone,
    role,
    status,
    enabled,
    created_at
)
SELECT
    'admin@airflytrip.cl',
    '$2a$12$T0wjlrNxqP.oZpEXgOALBOfL6pr3Kre0z7POdrUPBw8/ubUpQqnwy',
    'Admin',
    'AirFlyTrip',
    '11111111-1',
    '+56911111111',
    'ADMIN',
    'ACTIVE',
    TRUE,
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM app_user WHERE email = 'admin@airflytrip.cl'
);

INSERT INTO app_user (
    email,
    password_hash,
    first_name,
    last_name,
    document_number,
    phone,
    role,
    status,
    enabled,
    created_at
)
SELECT
    'operator@airflytrip.cl',
    '$2a$12$gh9A6L63QYoBkn49nhkdk.aT3ig8rRhSLDuCDS0D4/Ak85.RLbK7K',
    'Operator',
    'AirFlyTrip',
    '22222222-2',
    '+56922222222',
    'OPERATOR',
    'ACTIVE',
    TRUE,
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM app_user WHERE email = 'operator@airflytrip.cl'
);

INSERT INTO app_user (
    email,
    password_hash,
    first_name,
    last_name,
    document_number,
    phone,
    role,
    status,
    enabled,
    created_at
)
SELECT
    'client@airflytrip.cl',
    '$2a$12$6HYOrN7JC1nSl7mzV5b1LencLBn6e..gzmIB4qrNspkyzA6W3uAf6',
    'Client',
    'AirFlyTrip',
    '33333333-3',
    '+56933333333',
    'CLIENT',
    'ACTIVE',
    TRUE,
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM app_user WHERE email = 'client@airflytrip.cl'
);
