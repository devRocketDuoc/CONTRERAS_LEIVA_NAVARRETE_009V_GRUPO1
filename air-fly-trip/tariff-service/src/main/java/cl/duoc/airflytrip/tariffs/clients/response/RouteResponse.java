package cl.duoc.airflytrip.tariffs.clients.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RouteResponse {

    private Long id;
    private Long originTerminalId;
    private Long destinationTerminalId;
    private BigDecimal distanceKm;
    private Integer estimatedMinutes;
    private Boolean active;
}