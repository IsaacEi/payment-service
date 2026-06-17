package com.bancobase.payments.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreatePaymentRequest(
        @NotBlank @Size(max = 150) String concept,
        @NotNull @Min(1) Integer productQuantity,
        @NotBlank @Size(max = 120) String payer,
        @NotBlank @Size(max = 120) String payee,
        @NotNull @DecimalMin(value = "0.01") BigDecimal totalAmount
) {}
