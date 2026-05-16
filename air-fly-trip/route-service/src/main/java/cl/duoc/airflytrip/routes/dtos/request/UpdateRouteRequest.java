package cl.duoc.airflytrip.routes.dtos.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateRouteRequest {

    @NotNull(message = "Origin terminal id is required")
    private Long originTerminalId;

    @NotNull(message = "Destination terminal id is required")
    private Long destinationTerminalId;

    @NotNull(message = "Distance is required")
    @DecimalMin(value = "0.01", message = "Distance must be greater than 0")
    private BigDecimal distanceKm;

    @NotNull(message = "Estimated minutes is required")
    @Min(value = 1, message = "Estimated minutes must be greater than 0")
    private Integer estimatedMinutes;

    private Boolean active;
}
