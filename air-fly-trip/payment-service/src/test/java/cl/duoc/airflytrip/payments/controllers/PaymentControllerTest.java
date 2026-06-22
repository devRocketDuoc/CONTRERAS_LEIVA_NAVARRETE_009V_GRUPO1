package cl.duoc.airflytrip.payments.controllers;

import cl.duoc.airflytrip.payments.dtos.request.CreatePaymentRequest;
import cl.duoc.airflytrip.payments.dtos.request.UpdatePaymentStatusRequest;
import cl.duoc.airflytrip.payments.dtos.response.PaymentResponse;
import cl.duoc.airflytrip.payments.services.PaymentService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

  @Mock
  private PaymentService paymentService;

  @InjectMocks
  private PaymentController paymentController;

  @Test
  void findAllShouldReturnPayments() {

    when(paymentService.findAll()).thenReturn(List.of(paymentResponse(1L, "PENDING")));

    ResponseEntity<List<PaymentResponse>> result = paymentController.findAll();

    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertNotNull(result.getBody());
    assertEquals(1, result.getBody().size());
    assertEquals(1L, result.getBody().get(0).getId());
  }

  @Test
  void createShouldReturnCreatedPayment() {

    CreatePaymentRequest request = new CreatePaymentRequest();
    request.setTripId(24L);
    request.setAmount(new BigDecimal("12990.00"));
    request.setPaymentMethod("TARJETA_CREDITO");

    PaymentResponse responseSimulado = paymentResponse(41L, "PENDING");

    when(paymentService.create(any(CreatePaymentRequest.class))).thenReturn(responseSimulado);

    ResponseEntity<PaymentResponse> result = paymentController.create(request);

    assertEquals(HttpStatus.CREATED, result.getStatusCode());
    assertNotNull(result.getBody());
    assertEquals(41L, result.getBody().getId());
    assertEquals("PENDING", result.getBody().getStatus());
  }

  @Test
  void updateStatusShouldReturnUpdatedPayment() {

    UpdatePaymentStatusRequest request = new UpdatePaymentStatusRequest();
    request.setStatus("APPROVED");

    PaymentResponse responseSimulado = paymentResponse(41L, "APPROVED");

    when(paymentService.updateStatus(41L, request)).thenReturn(responseSimulado);

    ResponseEntity<PaymentResponse> result = paymentController.updateStatus(41L, request);

    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertNotNull(result.getBody());
    assertEquals("APPROVED", result.getBody().getStatus());
  }

  @Test
  void findByIdShouldReturnPayment() {

    when(paymentService.findById(41L)).thenReturn(paymentResponse(41L, "PENDING"));

    ResponseEntity<PaymentResponse> result = paymentController.findById(41L);

    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertNotNull(result.getBody());
    assertEquals(41L, result.getBody().getId());
  }

  private PaymentResponse paymentResponse(Long id, String status) {
    return PaymentResponse.builder()
        .id(id)
        .tripId(24L)
        .reservationId(null)
        .amount(new BigDecimal("12990.00"))
        .paymentMethod("TARJETA_CREDITO")
        .status(status)
        .paidAt("APPROVED".equals(status) ? LocalDateTime.of(2026, 6, 20, 14, 5) : null)
        .createdAt(LocalDateTime.of(2026, 6, 20, 13, 58))
        .build();
  }
}
