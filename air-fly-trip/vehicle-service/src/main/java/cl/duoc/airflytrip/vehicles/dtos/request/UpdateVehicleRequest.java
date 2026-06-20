package cl.duoc.airflytrip.vehicles.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos requeridos para actualizar completamente un vehiculo existente.")
public class UpdateVehicleRequest {

    @NotBlank(message = "Vehicle code is required")
    @Schema(description = "Codigo interno unico del vehiculo.", example = "VEH-001")
    private String code;

    @NotBlank(message = "Vehicle model is required")
    @Schema(description = "Modelo comercial del vehiculo.", example = "BYD Dolphin")
    private String model;

    @Schema(description = "Estado operativo del vehiculo.", example = "IN_MAINTENANCE")
    private String status;

    @NotNull(message = "Battery percentage is required")
    @Min(value = 0, message = "Battery percentage must be greater than or equal to 0")
    @Max(value = 100, message = "Battery percentage must be less than or equal to 100")
    @Schema(description = "Porcentaje actual de bateria del vehiculo.", example = "65")
    private Integer batteryPercentage;

    @NotNull(message = "Terminal id is required")
    @Schema(description = "Identificador del terminal donde quedara asociado el vehiculo.", example = "4")
    private Long terminalId;

    @Schema(description = "Identificador de la estacion de carga asociada, si corresponde.", example = "9")
    private Long chargingStationId;

    @Schema(description = "Indica si el vehiculo permanece activo.", example = "true")
    private Boolean active;
}
