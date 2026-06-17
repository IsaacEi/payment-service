package com.bancobase.payments.messaging;

import com.bancobase.payments.dto.PaymentStatusChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AuditConsumer {
    private static final Logger log = LoggerFactory.getLogger(AuditConsumer.class);

    @RabbitListener(queues = "${app.rabbitmq.audit-queue}")
    public void consume(PaymentStatusChangedEvent event) {
        log.info("AUDIT consumer received event: {}", event);
    }
}
