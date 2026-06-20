package cl.duoc.airflytrip.payments.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import cl.duoc.airflytrip.payments.clients.ReservationClient;
import cl.duoc.airflytrip.payments.clients.TripClient;
import cl.duoc.airflytrip.payments.clients.response.TripResponse;
import cl.duoc.airflytrip.payments.dtos.request.CreatePaymentRequest;
import cl.duoc.airflytrip.payments.dtos.request.UpdatePaymentStatusRequest;
import cl.duoc.airflytrip.payments.dtos.response.PaymentResponse;
import cl.duoc.airflytrip.payments.exceptions.BadRequestException;
import cl.duoc.airflytrip.payments.models.Payment;
import cl.duoc.airflytrip.payments.repositories.PaymentRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

  @Mock
  private PaymentRepository paymentRepository;

  @Mock
  private TripClient tripClient;

  @Mock
  private ReservationClient reservationClient;

  @InjectMocks
  private PaymentService paymentService;

  @Test
  void testCrearPagoExitosoParaViaje() {

    CreatePaymentRequest request = new CreatePaymentRequest();
    request.setTripId(24L);
    request.setAmount(new BigDecimal("12990.00"));
    request.setPaymentMethod("TARJETA_CREDITO");

    when(tripClient.findById(24L)).thenReturn(tripResponse(24L));
    when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
      Payment payment = invocation.getArgument(0);
      payment.setId(41L);
      return payment;
    });

    PaymentResponse response = paymentService.create(request);

    assertNotNull(response);
    assertEquals(41L, response.getId());
    assertEquals("PENDING", response.getStatus());
    assertEquals(new BigDecimal("12990.00"), response.getAmount());
    assertNull(response.getPaidAt());
  }

  @Test
  void testCrearPagoFallaSinReferencia() {

    CreatePaymentRequest request = new CreatePaymentRequest();
    request.setAmount(new BigDecimal("12990.00"));
    request.setPaymentMethod("TARJETA_CREDITO");

    Exception exception = null;

    try {
      paymentService.create(request);
    } catch (Exception ex) {
      exception = ex;
    }

    assertNotNull(exception);
    assertEquals(BadRequestException.class, exception.getClass());
    assertEquals("Payment must be associated with a trip or a reservation", exception.getMessage());
  }

  @Test
  void testActualizarEstadoAprobadoAsignaFechaPago() {

    UpdatePaymentStatusRequest request = new UpdatePaymentStatusRequest();
    request.setStatus("APPROVED");

    Payment existing = payment(41L, "PENDING");

    when(paymentRepository.findById(41L)).thenReturn(Optional.of(existing));
    when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

    PaymentResponse response = paymentService.updateStatus(41L, request);

    assertNotNull(response);
    assertEquals("APPROVED", response.getStatus());
    assertNotNull(response.getPaidAt());
  }

  @Test
  void testActualizarEstadoFallaSiYaEstaAprobado() {

    when(paymentRepository.findById(41L)).thenReturn(Optional.of(payment(41L, "APPROVED")));

    UpdatePaymentStatusRequest request = new UpdatePaymentStatusRequest();
    request.setStatus("REJECTED");

    Exception exception = null;

    try {
      paymentService.updateStatus(41L, request);
    } catch (Exception ex) {
      exception = ex;
    }

    assertNotNull(exception);
    assertEquals(BadRequestException.class, exception.getClass());
    assertEquals("Approved payments cannot change status", exception.getMessage());
  }

  private Payment payment(Long id, String status) {
    return Payment.builder()
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

  private TripResponse tripResponse(Long id) {
    TripResponse response = new TripResponse();
    response.setId(id);
    response.setUserId(15L);
    response.setVehicleId(8L);
    response.setOriginTerminalId(2L);
    response.setDestinationTerminalId(5L);
    response.setRouteId(11L);
    response.setStatus("REQUESTED");
    response.setScheduledAt(LocalDateTime.of(2026, 6, 21, 9, 30));
    response.setActive(true);
    response.setCreatedAt(LocalDateTime.of(2026, 6, 20, 18, 10));
    return response;
  }
}
