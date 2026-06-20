package cl.duoc.airflytrip.trips.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos requeridos para crear un nuevo viaje en el sistema.")
public class CreateTripRequest {

    @NotNull(message = "User id is required")
    @Schema(description = "Identificador del usuario asociado al viaje.", example = "15")
    private Long userId;

    @Schema(description = "Identificador del vehiculo asignado al viaje, si ya fue definido.", example = "8")
    private Long vehicleId;

    @NotNull(message = "Origin terminal id is required")
    @Schema(description = "Identificador del terminal de origen.", example = "2")
    private Long originTerminalId;

    @NotNull(message = "Destination terminal id is required")
    @Schema(description = "Identificador del terminal de destino.", example = "5")
    private Long destinationTerminalId;

    @NotNull(message = "Route id is required")
    @Schema(description = "Identificador de la ruta asociada al viaje.", example = "11")
    private Long routeId;

    @NotNull(message = "Scheduled date is required")
    @FutureOrPresent(message = "Scheduled date must be now or in the future")
    @Schema(description = "Fecha y hora programada para el viaje.", example = "2026-06-21T09:30:00")
    private LocalDateTime scheduledAt;
}

