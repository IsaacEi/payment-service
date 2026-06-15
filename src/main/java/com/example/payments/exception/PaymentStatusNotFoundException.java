package com.example.payments.exception;

public class PaymentStatusNotFoundException extends RuntimeException {
    public PaymentStatusNotFoundException(String code) {
        super("Payment status not found or inactive: " + code);
    }
}
