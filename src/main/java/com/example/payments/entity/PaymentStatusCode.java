package com.example.payments.entity;

public final class PaymentStatusCode {
    public static final String PENDING = "PENDING";
    public static final String PROCESSING = "PROCESSING";
    public static final String PAID = "PAID";
    public static final String REJECTED = "REJECTED";
    public static final String CANCELLED = "CANCELLED";

    private PaymentStatusCode() {}
}
