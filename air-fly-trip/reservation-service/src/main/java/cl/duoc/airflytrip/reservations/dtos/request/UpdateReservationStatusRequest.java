package cl.duoc.airflytrip.reservations.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos requeridos para actualizar el estado de una reserva.")
public class UpdateReservationStatusRequest {

    @NotBlank(message = "Status is required")
    @Schema(description = "Nuevo estado que se asignara a la reserva.", example = "CONFIRMED")
    private String status;
}
