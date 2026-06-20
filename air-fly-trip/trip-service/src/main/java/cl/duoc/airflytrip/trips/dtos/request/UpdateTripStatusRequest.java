package cl.duoc.airflytrip.trips.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos requeridos para actualizar el estado de un viaje.")
public class UpdateTripStatusRequest {

    @NotBlank(message = "Status is required")
    @Schema(description = "Nuevo estado operativo del viaje.", example = "IN_PROGRESS")
    private String status;
}
