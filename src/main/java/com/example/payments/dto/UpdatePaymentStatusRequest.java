package com.example.payments.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePaymentStatusRequest(
        @NotBlank @Size(max = 30) String statusCode
) {}
