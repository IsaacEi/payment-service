package com.bancobase.payments.repository;

import com.bancobase.payments.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Long> {
    Optional<PaymentStatus> findByCodeAndActiveTrue(String code);
}
