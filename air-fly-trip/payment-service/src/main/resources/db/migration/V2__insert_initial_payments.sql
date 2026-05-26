INSERT INTO payment (
    trip_id,
    reservation_id,
    amount,
    payment_method,
    status,
    paid_at,
    created_at
)
SELECT
    1,
    NULL,
    18780.00,
    'CREDIT_CARD',
    'PENDING',
    NULL,
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM payment WHERE trip_id = 1 AND status = 'PENDING'
);

INSERT INTO payment (
    trip_id,
    reservation_id,
    amount,
    payment_method,
    status,
    paid_at,
    created_at
)
SELECT
    2,
    NULL,
    28070.00,
    'DEBIT_CARD',
    'APPROVED',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM payment WHERE trip_id = 2 AND status = 'APPROVED'
);

INSERT INTO payment (
    trip_id,
    reservation_id,
    amount,
    payment_method,
    status,
    paid_at,
    created_at
)
SELECT
    NULL,
    1,
    18780.00,
    'BANK_TRANSFER',
    'APPROVED',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM payment WHERE reservation_id = 1 AND status = 'APPROVED'
);

INSERT INTO payment (
    trip_id,
    reservation_id,
    amount,
    payment_method,
    status,
    paid_at,
    created_at
)
SELECT
    NULL,
    2,
    28070.00,
    'CREDIT_CARD',
    'REJECTED',
    NULL,
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM payment WHERE reservation_id = 2 AND status = 'REJECTED'
);
