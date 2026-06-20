package cl.duoc.airflytrip.routes.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Informacion resumida de una ruta del sistema.")
public class RouteResponse {

    @Schema(description = "Identificador unico de la ruta.", example = "9")
    private Long id;

    @Schema(description = "Identificador del terminal de origen.", example = "1")
    private Long originTerminalId;

    @Schema(description = "Identificador del terminal de destino.", example = "4")
    private Long destinationTerminalId;

    @Schema(description = "Distancia total de la ruta en kilometros.", example = "12.5")
    private BigDecimal distanceKm;

    @Schema(description = "Tiempo estimado de viaje en minutos.", example = "35")
    private Integer estimatedMinutes;

    @Schema(description = "Indica si la ruta se encuentra activa.", example = "true")
    private Boolean active;
}

