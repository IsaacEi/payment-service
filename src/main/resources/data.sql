IF NOT EXISTS (SELECT 1 FROM payment_statuses WHERE code = 'PENDING')
INSERT INTO payment_statuses (code, name, description, active, created_at, updated_at)
VALUES ('PENDING', 'Pendiente', 'Pago creado pendiente de procesamiento', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

IF NOT EXISTS (SELECT 1 FROM payment_statuses WHERE code = 'PROCESSING')
INSERT INTO payment_statuses (code, name, description, active, created_at, updated_at)
VALUES ('PROCESSING', 'Procesando', 'Pago en proceso', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

IF NOT EXISTS (SELECT 1 FROM payment_statuses WHERE code = 'PAID')
INSERT INTO payment_statuses (code, name, description, active, created_at, updated_at)
VALUES ('PAID', 'Pagado', 'Pago completado correctamente', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

IF NOT EXISTS (SELECT 1 FROM payment_statuses WHERE code = 'REJECTED')
INSERT INTO payment_statuses (code, name, description, active, created_at, updated_at)
VALUES ('REJECTED', 'Rechazado', 'Pago rechazado', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

IF NOT EXISTS (SELECT 1 FROM payment_statuses WHERE code = 'CANCELLED')
INSERT INTO payment_statuses (code, name, description, active, created_at, updated_at)
VALUES ('CANCELLED', 'Cancelado', 'Pago cancelado', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
