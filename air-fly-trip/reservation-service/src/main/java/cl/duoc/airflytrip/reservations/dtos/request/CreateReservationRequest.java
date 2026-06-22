package cl.duoc.airflytrip.reservations.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos requeridos para crear una nueva reserva en el sistema.")
public class CreateReservationRequest {

    @NotNull(message = "User id is required")
    @Schema(description = "Identificador del usuario que realiza la reserva.", example = "12")
    private Long userId;

    @NotNull(message = "Route id is required")
    @Schema(description = "Identificador de la ruta asociada a la reserva.", example = "8")
    private Long routeId;

    @NotNull(message = "Origin terminal id is required")
    @Schema(description = "Identificador del terminal de origen de la reserva.", example = "3")
    private Long originTerminalId;

    @NotNull(message = "Destination terminal id is required")
    @Schema(description = "Identificador del terminal de destino de la reserva.", example = "5")
    private Long destinationTerminalId;

    @NotNull(message = "Reserved date is required")
    @Future(message = "Reserved date must be in the future")
    @Schema(description = "Fecha y hora programada para la reserva.", example = "2026-07-15T14:30:00")
    private LocalDateTime reservedAt;
}
