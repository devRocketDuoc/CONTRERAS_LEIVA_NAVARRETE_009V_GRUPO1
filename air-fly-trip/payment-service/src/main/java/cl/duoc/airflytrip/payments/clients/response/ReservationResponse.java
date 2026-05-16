package cl.duoc.airflytrip.payments.clients.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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
