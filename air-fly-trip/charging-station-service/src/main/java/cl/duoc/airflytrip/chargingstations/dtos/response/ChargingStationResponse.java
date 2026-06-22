package cl.duoc.airflytrip.chargingstations.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Informacion resumida de una estacion de carga del sistema.")
public class ChargingStationResponse {

    @Schema(description = "Identificador unico de la estacion de carga.", example = "15")
    private Long id;

    @Schema(description = "Codigo interno de la estacion de carga.", example = "CS-015")
    private String code;

    @Schema(description = "Nombre comercial de la estacion de carga.", example = "Estacion Centro")
    private String name;

    @Schema(description = "Identificador del terminal asociado a la estacion de carga.", example = "3")
    private Long terminalId;

    @Schema(description = "Capacidad total de cupos de la estacion de carga.", example = "12")
    private Integer capacity;

    @Schema(description = "Cantidad de cupos actualmente disponibles.", example = "5")
    private Integer availableSlots;

    @Schema(description = "Estado operativo actual de la estacion de carga.", example = "AVAILABLE")
    private String status;

    @Schema(description = "Indica si la estacion de carga se encuentra activa.", example = "true")
    private Boolean active;
}
