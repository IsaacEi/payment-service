/*
  Script de inicialización para SQL Server.
  Crea la base de datos, tablas y semilla inicial de estatus.
*/

IF DB_ID('$(DATABASE_NAME)') IS NULL
BEGIN
    EXEC('CREATE DATABASE [$(DATABASE_NAME)]');
END
GO

USE [$(DATABASE_NAME)];
GO

IF OBJECT_ID('dbo.payment_statuses', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.payment_statuses (
        id BIGINT IDENTITY(1,1) NOT NULL,
        code VARCHAR(30) NOT NULL,
        name VARCHAR(80) NOT NULL,
        description VARCHAR(255) NULL,
        active BIT NOT NULL CONSTRAINT df_payment_statuses_active DEFAULT 1,
        created_at DATETIME2 NOT NULL CONSTRAINT df_payment_statuses_created_at DEFAULT SYSUTCDATETIME(),
        updated_at DATETIME2 NOT NULL CONSTRAINT df_payment_statuses_updated_at DEFAULT SYSUTCDATETIME(),
        CONSTRAINT pk_payment_statuses PRIMARY KEY (id),
        CONSTRAINT uk_payment_statuses_code UNIQUE (code)
    );
END
GO

IF OBJECT_ID('dbo.payments', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.payments (
        id BIGINT IDENTITY(1,1) NOT NULL,
        concept VARCHAR(150) NOT NULL,
        product_quantity INT NOT NULL,
        payer VARCHAR(120) NOT NULL,
        payee VARCHAR(120) NOT NULL,
        total_amount DECIMAL(18,2) NOT NULL,
        status_id BIGINT NOT NULL,
        created_at DATETIME2 NOT NULL CONSTRAINT df_payments_created_at DEFAULT SYSUTCDATETIME(),
        updated_at DATETIME2 NOT NULL CONSTRAINT df_payments_updated_at DEFAULT SYSUTCDATETIME(),
        CONSTRAINT pk_payments PRIMARY KEY (id),
        CONSTRAINT fk_payments_status FOREIGN KEY (status_id) REFERENCES dbo.payment_statuses(id),
        CONSTRAINT ck_payments_product_quantity CHECK (product_quantity > 0),
        CONSTRAINT ck_payments_total_amount CHECK (total_amount >= 0)
    );
END
GO

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_payments_status_id')
BEGIN
    CREATE INDEX ix_payments_status_id ON dbo.payments(status_id);
END
GO

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_payments_created_at')
BEGIN
    CREATE INDEX ix_payments_created_at ON dbo.payments(created_at);
END
GO

MERGE dbo.payment_statuses AS target
USING (VALUES
    ('PENDING', 'Pendiente', 'Pago creado pendiente de procesamiento', 1),
    ('PROCESSING', 'Procesando', 'Pago en proceso', 1),
    ('PAID', 'Pagado', 'Pago completado correctamente', 1),
    ('REJECTED', 'Rechazado', 'Pago rechazado', 1),
    ('CANCELLED', 'Cancelado', 'Pago cancelado', 1)
) AS source (code, name, description, active)
ON target.code = source.code
WHEN NOT MATCHED THEN
    INSERT (code, name, description, active)
    VALUES (source.code, source.name, source.description, source.active)
WHEN MATCHED THEN
    UPDATE SET
        target.name = source.name,
        target.description = source.description,
        target.active = source.active,
        target.updated_at = SYSUTCDATETIME();
GO
