# Payment Service

Proyecto de pagos con Java 17, Spring Boot 3.2.5, SQL Server, RabbitMQ, JUnit y Docker.

## Servicios

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/payments` | Dar de alta un pago |
| GET | `/api/payments` | Listar pagos con filtros, paginación y ordenamiento |
| GET | `/api/payments/{id}` | Consultar un pago |
| GET | `/api/payments/{id}/status` | Verificar estatus del pago |
| PATCH | `/api/payments/{id}/status` | Cambiar estatus del pago y publicar evento RabbitMQ |

## Diseño de base de datos

El proyecto usa SQL Server y el estatus está normalizado en una tabla catálogo.

- `payments`: almacena los pagos.
- `payment_statuses`: almacena los estatus disponibles.
- `payments.status_id`: llave foránea hacia `payment_statuses.id`.

Esto permite agregar, desactivar o cambiar nombres de estatus sin modificar la tabla principal de pagos.

## Variables de entorno

El proyecto incluye una semilla de variables:

```bash
cp .env.example .env
```

Después puedes modificar `.env` sin tocar `docker-compose.yml` ni `application.yml`.

## Ejecutar con Docker

```bash
docker compose up --build
```

También puedes usar:

```bash
make up
```

El servicio queda en:

```bash
http://localhost:8080
```

## Crear base de datos y tablas

La forma más fácil es usando Docker Compose. El servicio `sqlserver-init` ejecuta automáticamente:

```bash
scripts/init-db.sql
```

Ese script crea:

- Base de datos `paymentsdb`
- Tabla `payment_statuses`
- Tabla `payments`
- Índices
- Semilla inicial de estatus

Si quieres ejecutarlo manualmente contra un SQL Server local, usa:

```bash
./scripts/create-db.sh
```

O con Docker:

```bash
docker compose run --rm sqlserver-init
```

## RabbitMQ Management

- URL: http://localhost:15672
- User: `guest`
- Password: `guest`

## SQL Server

Valores por defecto desde `.env.example`:

- Host: `localhost`
- Port: `1433`
- User: `sa`
- Password: `YourStrong!Passw0rd`
- Database: `paymentsdb`

## Crear pago

```bash
curl -X POST http://localhost:8080/api/payments \
  -H "Content-Type: application/json" \
  -d '{
    "concept": "Compra de productos",
    "productQuantity": 3,
    "payer": "Isaac Cisneros",
    "payee": "Proveedor ABC",
    "totalAmount": 1500.75
  }'
```

## Listar pagos

```bash
curl "http://localhost:8080/api/payments?page=0&size=10&sort=createdAt,desc"
```

## Listar pagos con filtros

```bash
curl "http://localhost:8080/api/payments?status=PENDING&payer=Isaac&payee=Proveedor&page=0&size=10"
```

Filtros disponibles:

- `status`: código del estatus, por ejemplo `PENDING`, `PAID`, `CANCELLED`.
- `payer`: búsqueda parcial por quien realiza el pago.
- `payee`: búsqueda parcial por a quien se le paga.
- `page`: página iniciando en `0`.
- `size`: cantidad de registros por página.
- `sort`: campo y dirección, por ejemplo `createdAt,desc` o `id,asc`.


## Cambiar estatus

```bash
curl -X PATCH http://localhost:8080/api/payments/1/status \
  -H "Content-Type: application/json" \
  -d '{ "status": "PAID" }'
```

Al cambiar el estatus, se publica un mensaje en RabbitMQ y lo reciben dos consumers:

- `payments.status.audit.queue`
- `payments.status.notification.queue`

## Estatus disponibles

- `PENDING`
- `PROCESSING`
- `PAID`
- `REJECTED`
- `CANCELLED`

## Entregables incluidos

- Código Java Spring Boot
- `.env.example` como semilla de variables de entorno
- `docker-compose.yml` usando variables de entorno
- `scripts/init-db.sql` para crear base, tablas y semilla
- `scripts/create-db.sh` para ejecutar el script manualmente
- `Makefile` con comandos rápidos
- Documentación de base de datos relacional: `docs/database-design.md`
- Diagrama entidad-relación: `docs/entity-relationship.md`
- Script SQL Server DDL: `docs/database-ddl.sql`
- Dockerfile
- Collection Postman: `postman/payment-service.postman_collection.json`
- Documento RabbitMQ: `docs/rabbitmq-definition.md`

## Nota 
- Si el puerto `8080` marca error cambiarlo a un puerto disponible ejemplo 8081 en las variables de entorno `APP_PORT=8081`

