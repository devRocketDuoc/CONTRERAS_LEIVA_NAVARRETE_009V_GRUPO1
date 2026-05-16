package cl.duoc.airflytrip.reservations.dtos.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReservationResponse {

    private Long id;

    private Long userId;

    private Long routeId;

    private Long originTerminalId;

    private Long destinationTerminalId;

    private LocalDateTime reservedAt;

    private String status;

    private Boolean active;

    private LocalDateTime createdAt;
}