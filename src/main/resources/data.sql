INSERT INTO payment_statuses (code, name, description, active, created_at, updated_at)
SELECT 'PENDING', 'Pendiente', 'Pago creado pendiente de procesamiento', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM payment_statuses WHERE code = 'PENDING'
);

INSERT INTO payment_statuses (code, name, description, active, created_at, updated_at)
SELECT 'PROCESSING', 'Procesando', 'Pago en proceso', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM payment_statuses WHERE code = 'PROCESSING'
);

INSERT INTO payment_statuses (code, name, description, active, created_at, updated_at)
SELECT 'PAID', 'Pagado', 'Pago completado', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM payment_statuses WHERE code = 'PAID'
);

INSERT INTO payment_statuses (code, name, description, active, created_at, updated_at)
SELECT 'CANCELLED', 'Cancelado', 'Pago cancelado', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM payment_statuses WHERE code = 'CANCELLED'
);