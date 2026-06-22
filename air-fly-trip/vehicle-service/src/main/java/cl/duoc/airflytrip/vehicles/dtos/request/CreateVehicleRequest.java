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
@Schema(description = "Datos requeridos para crear un nuevo vehiculo en el sistema.")
public class CreateVehicleRequest {

    @NotBlank(message = "Vehicle code is required")
    @Schema(description = "Codigo interno unico del vehiculo.", example = "VEH-001")
    private String code;

    @NotBlank(message = "Vehicle model is required")
    @Schema(description = "Modelo comercial del vehiculo.", example = "BYD Dolphin")
    private String model;

    @Schema(description = "Estado operativo inicial del vehiculo.", example = "AVAILABLE")
    private String status;

    @NotNull(message = "Battery percentage is required")
    @Min(value = 0, message = "Battery percentage must be greater than or equal to 0")
    @Max(value = 100, message = "Battery percentage must be less than or equal to 100")
    @Schema(description = "Porcentaje actual de bateria del vehiculo.", example = "82")
    private Integer batteryPercentage;

    @NotNull(message = "Terminal id is required")
    @Schema(description = "Identificador del terminal donde se encuentra el vehiculo.", example = "3")
    private Long terminalId;

    @Schema(description = "Identificador de la estacion de carga asociada, si corresponde.", example = "7")
    private Long chargingStationId;

    @Schema(description = "Indica si el vehiculo se encuentra activo.", example = "true")
    private Boolean active;
}
