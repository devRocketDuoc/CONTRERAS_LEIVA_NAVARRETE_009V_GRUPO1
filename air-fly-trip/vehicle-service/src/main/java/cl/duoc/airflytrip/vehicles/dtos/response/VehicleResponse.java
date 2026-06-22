package cl.duoc.airflytrip.vehicles.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Informacion resumida de un vehiculo del sistema.")
public class VehicleResponse {

    @Schema(description = "Identificador unico del vehiculo.", example = "12")
    private Long id;

    @Schema(description = "Codigo interno del vehiculo.", example = "VEH-001")
    private String code;

    @Schema(description = "Modelo comercial del vehiculo.", example = "BYD Dolphin")
    private String model;

    @Schema(description = "Estado operativo del vehiculo.", example = "AVAILABLE")
    private String status;

    @Schema(description = "Porcentaje actual de bateria del vehiculo.", example = "82")
    private Integer batteryPercentage;

    @Schema(description = "Identificador del terminal asociado al vehiculo.", example = "3")
    private Long terminalId;

    @Schema(description = "Identificador de la estacion de carga asociada, si corresponde.", example = "7")
    private Long chargingStationId;

    @Schema(description = "Indica si el vehiculo se encuentra activo.", example = "true")
    private Boolean active;
}
