package com.bancobase.payments.controller;

import com.bancobase.payments.dto.CreatePaymentRequest;
import com.bancobase.payments.dto.PaymentResponse;
import com.bancobase.payments.dto.PaymentStatusResponse;
import com.bancobase.payments.dto.UpdatePaymentStatusRequest;
import com.bancobase.payments.dto.*;
import com.bancobase.payments.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<PaymentResponse>> findAll(
            @RequestParam(required = false) String payer,
            @RequestParam(required = false) String payee,
            @RequestParam(required = false) String status,
            @NonNull Pageable pageable) {
        return ResponseEntity.ok(service.findAll(payer, payee, status, pageable));
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> create(@Valid @RequestBody CreatePaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<PaymentStatusResponse> getStatus(@PathVariable Long id) {
        return ResponseEntity.ok(service.getStatus(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PaymentResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePaymentStatusRequest request) {
        return ResponseEntity.ok(service.updateStatus(id, request));
    }
}
