package cl.duoc.airflytrip.payments.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Informacion resumida de un pago del sistema.")
public class PaymentResponse {

    @Schema(description = "Identificador unico del pago.", example = "41")
    private Long id;

    @Schema(description = "Identificador del viaje asociado al pago, si corresponde.", example = "24")
    private Long tripId;

    @Schema(description = "Identificador de la reserva asociada al pago, si corresponde.", example = "102")
    private Long reservationId;

    @Schema(description = "Monto total del pago.", example = "12990.00")
    private BigDecimal amount;

    @Schema(description = "Metodo utilizado para realizar el pago.", example = "TARJETA_CREDITO")
    private String paymentMethod;

    @Schema(description = "Estado actual del pago.", example = "PAID")
    private String status;

    @Schema(description = "Fecha y hora en que el pago fue confirmado.", example = "2026-06-20T14:05:00")
    private LocalDateTime paidAt;

    @Schema(description = "Fecha y hora de creacion del pago.", example = "2026-06-20T13:58:00")
    private LocalDateTime createdAt;
}
