package com.bancobase.payments.service;

import com.bancobase.payments.dto.*;
import com.bancobase.payments.dto.*;
import com.bancobase.payments.entity.Payment;
import com.bancobase.payments.entity.PaymentStatus;
import com.bancobase.payments.entity.PaymentStatusCode;
import com.bancobase.payments.exception.PaymentNotFoundException;
import com.bancobase.payments.exception.PaymentStatusNotFoundException;
import com.bancobase.payments.messaging.PaymentEventPublisher;
import com.bancobase.payments.repository.PaymentRepository;
import com.bancobase.payments.repository.PaymentStatusRepository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentStatusRepository statusRepository;
    private final PaymentEventPublisher eventPublisher;

    public PaymentService(PaymentRepository paymentRepository,
                          PaymentStatusRepository statusRepository,
                          PaymentEventPublisher eventPublisher) {
        this.paymentRepository = paymentRepository;
        this.statusRepository = statusRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional(readOnly = true)
    public Page<PaymentResponse> findAll(String payer, String payee, String status, @NonNull Pageable pageable) {
        Specification<Payment> spec = Specification.where(null);

        if (payer != null && !payer.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("payer")), "%" + payer.toLowerCase() + "%"));
        }

        if (payee != null && !payee.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("payee")), "%" + payee.toLowerCase() + "%"));
        }

        if (status != null && !status.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.upper(root.get("status").get("code")), status.toUpperCase()));
        }

        return paymentRepository.findAll(spec, pageable).map(this::toResponse);
    }

    @Transactional
    public PaymentResponse create(CreatePaymentRequest request) {
        PaymentStatus pendingStatus = findStatusByCode(PaymentStatusCode.PENDING);

        Payment payment = new Payment();
        payment.setConcept(request.concept());
        payment.setProductQuantity(request.productQuantity());
        payment.setPayer(request.payer());
        payment.setPayee(request.payee());
        payment.setTotalAmount(request.totalAmount());
        payment.setStatus(pendingStatus);
        return toResponse(paymentRepository.save(payment));
    }
    

    @Transactional(readOnly = true)
    public PaymentResponse findById(@NonNull Long id) {
        return paymentRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new PaymentNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public PaymentStatusResponse getStatus(@NonNull Long id) {
        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new PaymentNotFoundException(id));
        return new PaymentStatusResponse(payment.getId(), toStatusDto(payment.getStatus()));
    }

    @Transactional
    public PaymentResponse updateStatus(@NonNull Long id, UpdatePaymentStatusRequest request) {
        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new PaymentNotFoundException(id));
        PaymentStatus oldStatus = payment.getStatus();
        PaymentStatus newStatus = findStatusByCode(request.statusCode());

        payment.setStatus(newStatus);
        Payment saved = paymentRepository.save(payment);

        if (!oldStatus.getCode().equals(newStatus.getCode())) {
            eventPublisher.publishStatusChanged(
                    new PaymentStatusChangedEvent(saved.getId(), oldStatus.getCode(), newStatus.getCode(), LocalDateTime.now())
            );
        }
        return toResponse(saved);
    }

    private PaymentStatus findStatusByCode(String code) {
        return statusRepository.findByCodeAndActiveTrue(code)
                .orElseThrow(() -> new PaymentStatusNotFoundException(code));
    }

    private PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getConcept(),
                payment.getProductQuantity(),
                payment.getPayer(),
                payment.getPayee(),
                payment.getTotalAmount(),
                toStatusDto(payment.getStatus()),
                payment.getCreatedAt(),
                payment.getUpdatedAt()
        );
    }

    private PaymentStatusDto toStatusDto(PaymentStatus status) {
        return new PaymentStatusDto(status.getId(), status.getCode(), status.getName());
    }
}
