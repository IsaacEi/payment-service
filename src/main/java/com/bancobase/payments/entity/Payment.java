package com.bancobase.payments.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String concept;

    @Column(nullable = false)
    private Integer productQuantity;

    @Column(nullable = false, length = 120)
    private String payer;

    @Column(nullable = false, length = 120)
    private String payee;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal totalAmount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false, foreignKey = @ForeignKey(name = "fk_payments_status"))
    private PaymentStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getConcept() { return concept; }
    public void setConcept(String concept) { this.concept = concept; }
    public Integer getProductQuantity() { return productQuantity; }
    public void setProductQuantity(Integer productQuantity) { this.productQuantity = productQuantity; }
    public String getPayer() { return payer; }
    public void setPayer(String payer) { this.payer = payer; }
    public String getPayee() { return payee; }
    public void setPayee(String payee) { this.payee = payee; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
