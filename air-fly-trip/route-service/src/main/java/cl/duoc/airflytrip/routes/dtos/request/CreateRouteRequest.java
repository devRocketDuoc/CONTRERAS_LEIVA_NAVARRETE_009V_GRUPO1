package cl.duoc.airflytrip.routes.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos requeridos para crear una nueva ruta en el sistema.")
public class CreateRouteRequest {

    @NotNull(message = "Origin terminal id is required")
    @Schema(description = "Identificador del terminal de origen.", example = "1")
    private Long originTerminalId;

    @NotNull(message = "Destination terminal id is required")
    @Schema(description = "Identificador del terminal de destino.", example = "4")
    private Long destinationTerminalId;

    @NotNull(message = "Distance is required")
    @DecimalMin(value = "0.01", message = "Distance must be greater than 0")
    @Schema(description = "Distancia total de la ruta en kilometros.", example = "12.5")
    private BigDecimal distanceKm;

    @NotNull(message = "Estimated minutes is required")
    @Min(value = 1, message = "Estimated minutes must be greater than 0")
    @Schema(description = "Tiempo estimado de viaje en minutos.", example = "35")
    private Integer estimatedMinutes;

    @Schema(description = "Indica si la ruta se encuentra activa.", example = "true")
    private Boolean active;
}

