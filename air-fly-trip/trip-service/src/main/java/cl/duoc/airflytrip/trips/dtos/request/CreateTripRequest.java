package cl.duoc.airflytrip.trips.dtos.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateTripRequest {

    @NotNull(message = "User id is required")
    private Long userId;

    private Long vehicleId;

    @NotNull(message = "Origin terminal id is required")
    private Long originTerminalId;

    @NotNull(message = "Destination terminal id is required")
    private Long destinationTerminalId;

    @NotNull(message = "Route id is required")
    private Long routeId;

    @NotNull(message = "Scheduled date is required")
    @FutureOrPresent(message = "Scheduled date must be now or in the future")
    private LocalDateTime scheduledAt;
}

