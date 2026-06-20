package cl.duoc.airflytrip.vehicles.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos requeridos para actualizar el estado operativo de un vehiculo.")
public class UpdateVehicleStatusRequest {

    @NotBlank(message = "Status is required")
    @Schema(description = "Nuevo estado operativo del vehiculo.", example = "AVAILABLE")
    private String status;
}
