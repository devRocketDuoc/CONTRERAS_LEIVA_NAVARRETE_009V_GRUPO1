INSERT INTO notification (
    user_id,
    type,
    title,
    message,
    status,
    created_at
)
SELECT
    3,
    'RESERVATION',
    'Reserva registrada',
    'Tu reserva fue registrada correctamente en Air-Fly-Trip.',
    'SENT',
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM notification WHERE user_id = 3 AND type = 'RESERVATION' AND title = 'Reserva registrada'
);

INSERT INTO notification (
    user_id,
    type,
    title,
    message,
    status,
    created_at
)
SELECT
    3,
    'TRIP',
    'Viaje confirmado',
    'Tu viaje fue confirmado y será gestionado por nuestro sistema autónomo.',
    'PENDING',
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM notification WHERE user_id = 3 AND type = 'TRIP' AND title = 'Viaje confirmado'
);

INSERT INTO notification (
    user_id,
    type,
    title,
    message,
    status,
    created_at
)
SELECT
    3,
    'PAYMENT',
    'Pago aprobado',
    'El pago asociado a tu servicio fue aprobado correctamente.',
    'READ',
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM notification WHERE user_id = 3 AND type = 'PAYMENT' AND title = 'Pago aprobado'
);

INSERT INTO notification (
    user_id,
    type,
    title,
    message,
    status,
    created_at
)
SELECT
    2,
    'SYSTEM',
    'Alerta operacional',
    'Existe una actualización operacional pendiente de revisión.',
    'PENDING',
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM notification WHERE user_id = 2 AND type = 'SYSTEM' AND title = 'Alerta operacional'
);