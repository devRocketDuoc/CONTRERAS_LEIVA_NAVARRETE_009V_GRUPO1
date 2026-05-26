package cl.duoc.airflytrip.trips.dtos.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
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
