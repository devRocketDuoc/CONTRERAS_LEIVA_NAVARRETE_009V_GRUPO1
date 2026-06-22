package cl.duoc.airflytrip.chargingstations.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos requeridos para actualizar completamente una estacion de carga existente.")
public class UpdateChargingStationRequest {

    @NotBlank(message = "Charging station code is required")
    @Schema(description = "Codigo interno unico de la estacion de carga.", example = "CS-015")
    private String code;

    @NotBlank(message = "Charging station name is required")
    @Schema(description = "Nombre comercial de la estacion de carga.", example = "Estacion Centro Norte")
    private String name;

    @NotNull(message = "Terminal id is required")
    @Schema(description = "Identificador del terminal asociado a la estacion de carga.", example = "4")
    private Long terminalId;

    @NotNull(message = "Capacity is required")
    @Min(value = 0, message = "Capacity must be greater than or equal to 0")
    @Schema(description = "Capacidad total de cupos de la estacion de carga.", example = "14")
    private Integer capacity;

    @NotNull(message = "Available slots is required")
    @Min(value = 0, message = "Available slots must be greater than or equal to 0")
    @Schema(description = "Cantidad de cupos actualmente disponibles.", example = "6")
    private Integer availableSlots;

    @Schema(description = "Estado operativo de la estacion de carga.", example = "MAINTENANCE")
    private String status;

    @Schema(description = "Indica si la estacion de carga permanece activa.", example = "true")
    private Boolean active;
}
