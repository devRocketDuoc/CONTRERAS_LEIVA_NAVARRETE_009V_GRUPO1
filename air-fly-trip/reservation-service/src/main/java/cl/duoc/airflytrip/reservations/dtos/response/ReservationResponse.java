package cl.duoc.airflytrip.reservations.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Respuesta con la informacion detallada de una reserva.")
public class ReservationResponse {

    @Schema(description = "Identificador unico de la reserva.", example = "40")
    private Long id;

    @Schema(description = "Identificador del usuario asociado a la reserva.", example = "12")
    private Long userId;

    @Schema(description = "Identificador de la ruta reservada.", example = "8")
    private Long routeId;

    @Schema(description = "Identificador del terminal de origen.", example = "3")
    private Long originTerminalId;

    @Schema(description = "Identificador del terminal de destino.", example = "5")
    private Long destinationTerminalId;

    @Schema(description = "Fecha y hora programada para la reserva.", example = "2026-07-15T14:30:00")
    private LocalDateTime reservedAt;

    @Schema(description = "Estado actual de la reserva.", example = "PENDING")
    private String status;

    @Schema(description = "Indica si la reserva sigue activa.", example = "true")
    private Boolean active;

    @Schema(description = "Fecha y hora de creacion de la reserva.", example = "2026-06-21T10:15:00")
    private LocalDateTime createdAt;
}
