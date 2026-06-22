package cl.duoc.airflytrip.payments.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos requeridos para crear un nuevo pago en el sistema.")
public class CreatePaymentRequest {

    @Schema(description = "Identificador del viaje asociado al pago, si corresponde.", example = "24")
    private Long tripId;

    @Schema(description = "Identificador de la reserva asociada al pago, si corresponde.", example = "102")
    private Long reservationId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Schema(description = "Monto total del pago.", example = "12990.00")
    private BigDecimal amount;

    @NotBlank(message = "Payment method is required")
    @Schema(description = "Metodo utilizado para realizar el pago.", example = "TARJETA_CREDITO")
    private String paymentMethod;

    @Schema(description = "Estado inicial del pago.", example = "PENDING")
    private String status;
}
