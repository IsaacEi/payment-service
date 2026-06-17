package com.bancobase.payments.messaging;

import com.bancobase.payments.dto.PaymentStatusChangedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;

    public PaymentEventPublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${app.rabbitmq.exchange}") String exchange,
            @Value("${app.rabbitmq.status-routing-key}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public void publishStatusChanged(PaymentStatusChangedEvent event) {
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }
}
