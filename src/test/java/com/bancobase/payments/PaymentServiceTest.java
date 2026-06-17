package com.bancobase.payments;

import com.bancobase.payments.dto.CreatePaymentRequest;
import com.bancobase.payments.dto.UpdatePaymentStatusRequest;
import com.bancobase.payments.messaging.PaymentEventPublisher;
import com.bancobase.payments.repository.PaymentRepository;
import com.bancobase.payments.repository.PaymentStatusRepository;
import com.bancobase.payments.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@DataJpaTest
@Import(PaymentService.class)
class PaymentServiceTest {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentStatusRepository paymentStatusRepository;

    @MockBean
    private PaymentEventPublisher eventPublisher;

    @SuppressWarnings("null")
    @Test
    void shouldCreatePaymentWithPendingStatus() {
        var response = paymentService.create(new CreatePaymentRequest(
                "Compra de productos",
                2,
                "Isaac",
                "Empresa ABC",
                BigDecimal.valueOf(350.50)
        ));

        assertThat(response.id()).isNotNull();
        assertThat(response.status().code()).isEqualTo("PENDING");
        assertThat(paymentRepository.findById(response.id())).isPresent();
        assertThat(paymentStatusRepository.findByCodeAndActiveTrue("PENDING")).isPresent();
    }

    @Test
    void shouldListPaymentsWithFiltersAndPagination() {
        paymentService.create(new CreatePaymentRequest(
                "Compra de laptop",
                1,
                "Isaac Cisneros",
                "Dell",
                BigDecimal.valueOf(25000)
        ));

        paymentService.create(new CreatePaymentRequest(
                "Compra de monitor",
                2,
                "Carlos Pérez",
                "HP",
                BigDecimal.valueOf(8000)
        ));

        var page = paymentService.findAll("isaac", null, "PENDING", PageRequest.of(0, 10));

        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().get(0).payer()).isEqualTo("Isaac Cisneros");
        assertThat(page.getContent().get(0).status().code()).isEqualTo("PENDING");
    }


    @Test
    void shouldUpdateStatusAndPublishEvent() {
        var created = paymentService.create(new CreatePaymentRequest(
                "Servicio mensual",
                1,
                "Cliente Uno",
                "Proveedor Uno",
                BigDecimal.valueOf(999.99)
        ));

        var updated = paymentService.updateStatus(created.id(), new UpdatePaymentStatusRequest("PAID"));

        assertThat(updated.status().code()).isEqualTo("PAID");
        verify(eventPublisher).publishStatusChanged(org.mockito.ArgumentMatchers.any());
    }
}
