package com.example.payments.messaging;

import com.example.payments.dto.PaymentStatusChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {
    private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);

    @RabbitListener(queues = "${app.rabbitmq.notification-queue}")
    public void consume(PaymentStatusChangedEvent event) {
        log.info("NOTIFICATION consumer received event: {}", event);
    }
}
