# Definición RabbitMQ

## Exchange

| Nombre | Tipo | Durable |
|---|---|---|
| payments.exchange | direct | true |

## Routing key

| Evento | Routing key |
|---|---|
| Cambio de estatus de pago | payments.status.changed |

## Queues

| Queue | Uso |
|---|---|
| payments.status.audit.queue | Consumer para auditoría |
| payments.status.notification.queue | Consumer para notificaciones |

## Mensaje publicado

```json
{
  "paymentId": 1,
  "oldStatusCode": "PENDING",
  "newStatusCode": "PAID",
  "changedAt": "2026-06-11T19:30:00"
}
```

## Flujo

1. Cliente actualiza el estatus con `PATCH /api/payments/{id}/status`.
2. El servicio busca el nuevo estatus en `payment_statuses` y guarda la FK `payments.status_id` en SQL Server.
3. Si el estatus cambió, publica un evento en `payments.exchange`.
4. RabbitMQ enruta el mensaje con `payments.status.changed`.
5. Dos consumers reciben el mismo evento:
   - `AuditConsumer`
   - `NotificationConsumer`
