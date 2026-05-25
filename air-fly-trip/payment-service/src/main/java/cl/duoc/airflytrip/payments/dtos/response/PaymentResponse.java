package cl.duoc.airflytrip.payments.dtos.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class PaymentResponse {

    private Long id;

    private Long tripId;

    private Long reservationId;

    private BigDecimal amount;

    private String paymentMethod;

    private String status;

    private LocalDateTime paidAt;

    private LocalDateTime createdAt;
}
