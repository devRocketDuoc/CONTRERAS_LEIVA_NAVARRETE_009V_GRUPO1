package cl.duoc.airflytrip.payments.clients.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TripResponse {

    private Long id;
    private Long userId;
    private Long vehicleId;
    private Long originTerminalId;
    private Long destinationTerminalId;
    private Long routeId;
    private String status;
    private LocalDateTime scheduledAt;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private Boolean active;
    private LocalDateTime createdAt;
}
