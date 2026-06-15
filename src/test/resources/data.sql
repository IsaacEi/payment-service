INSERT INTO payment_statuses (id, code, name, description, active, created_at, updated_at)
VALUES (1, 'PENDING', 'Pendiente', 'Pago creado pendiente de procesamiento', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO payment_statuses (id, code, name, description, active, created_at, updated_at)
VALUES (2, 'PROCESSING', 'Procesando', 'Pago en proceso', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO payment_statuses (id, code, name, description, active, created_at, updated_at)
VALUES (3, 'PAID', 'Pagado', 'Pago completado correctamente', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO payment_statuses (id, code, name, description, active, created_at, updated_at)
VALUES (4, 'REJECTED', 'Rechazado', 'Pago rechazado', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO payment_statuses (id, code, name, description, active, created_at, updated_at)
VALUES (5, 'CANCELLED', 'Cancelado', 'Pago cancelado', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
