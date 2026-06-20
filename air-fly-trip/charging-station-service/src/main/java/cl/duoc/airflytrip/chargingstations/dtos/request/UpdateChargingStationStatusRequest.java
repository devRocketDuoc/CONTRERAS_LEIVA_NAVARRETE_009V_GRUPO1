package cl.duoc.airflytrip.chargingstations.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos requeridos para actualizar el estado de una estacion de carga.")
public class UpdateChargingStationStatusRequest {

    @NotBlank(message = "Status is required")
    @Schema(description = "Nuevo estado operativo de la estacion de carga.", example = "AVAILABLE")
    private String status;
}
