package com.bancobase.payments.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        String concept,
        Integer productQuantity,
        String payer,
        String payee,
        BigDecimal totalAmount,
        PaymentStatusDto status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
