package com.example.payments.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

@Configuration
public class RabbitMQConfig {
    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.status-routing-key}")
    private String routingKey;

    @Value("${app.rabbitmq.audit-queue}")
    private String auditQueue;

    @Value("${app.rabbitmq.notification-queue}")
    private String notificationQueue;

    @Bean
    DirectExchange paymentsExchange() {
        return new DirectExchange(exchange, true, false);
    }

    @Bean
    Queue auditQueue() {
        return QueueBuilder.durable(auditQueue).build();
    }

    @Bean
    Queue notificationQueue() {
        return QueueBuilder.durable(notificationQueue).build();
    }

    @Bean
    Binding auditBinding(Queue auditQueue, DirectExchange paymentsExchange) {
        return BindingBuilder.bind(auditQueue).to(paymentsExchange).with(routingKey);
    }

    @Bean
    Binding notificationBinding(Queue notificationQueue, DirectExchange paymentsExchange) {
        return BindingBuilder.bind(notificationQueue).to(paymentsExchange).with(routingKey);
    }

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(@NonNull ConnectionFactory connectionFactory, @NonNull MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        template.setConfirmCallback((correlationData, ack, cause) -> {

            if (ack) {

                System.out.println("RabbitMQ recibió el mensaje");

            } else {

                System.out.println("RabbitMQ NO recibió el mensaje: " + cause);

            }

        });

        template.setReturnsCallback(returned -> {

            System.out.println("Mensaje no llegó a ninguna cola: "

                    + returned.getMessage());

        });

        template.setMandatory(true);

        return template;
    }
}
