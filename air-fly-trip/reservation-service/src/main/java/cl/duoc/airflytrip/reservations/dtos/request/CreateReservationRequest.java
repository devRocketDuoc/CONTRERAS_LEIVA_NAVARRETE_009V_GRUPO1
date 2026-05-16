package cl.duoc.airflytrip.reservations.dtos.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateReservationRequest {

    @NotNull(message = "User id is required")
    private Long userId;

    @NotNull(message = "Route id is required")
    private Long routeId;

    @NotNull(message = "Origin terminal id is required")
    private Long originTerminalId;

    @NotNull(message = "Destination terminal id is required")
    private Long destinationTerminalId;

    @NotNull(message = "Reserved date is required")
    @Future(message = "Reserved date must be in the future")
    private LocalDateTime reservedAt;
}