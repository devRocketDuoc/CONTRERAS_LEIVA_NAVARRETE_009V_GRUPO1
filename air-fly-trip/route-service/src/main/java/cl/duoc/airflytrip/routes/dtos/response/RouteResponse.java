package cl.duoc.airflytrip.routes.dtos.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class RouteResponse {

    private Long id;

    private Long originTerminalId;

    private Long destinationTerminalId;

    private BigDecimal distanceKm;

    private Integer estimatedMinutes;

    private Boolean active;
}

