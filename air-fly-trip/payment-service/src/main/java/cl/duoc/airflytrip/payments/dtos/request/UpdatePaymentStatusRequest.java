package cl.duoc.airflytrip.payments.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos requeridos para actualizar el estado de un pago.")
public class UpdatePaymentStatusRequest {

    @NotBlank(message = "Status is required")
    @Schema(description = "Nuevo estado del pago.", example = "PAID")
    private String status;
}
