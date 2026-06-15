package com.example.payments.dto;

import java.time.LocalDateTime;

public record PaymentStatusChangedEvent(
        Long paymentId,
        String oldStatusCode,
        String newStatusCode,
        LocalDateTime changedAt
) {}
