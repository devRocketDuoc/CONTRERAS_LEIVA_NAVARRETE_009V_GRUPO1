package cl.duoc.airflytrip.trips.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Informacion resumida de un viaje del sistema.")
public class TripResponse {

    @Schema(description = "Identificador unico del viaje.", example = "24")
    private Long id;

    @Schema(description = "Identificador del usuario asociado al viaje.", example = "15")
    private Long userId;

    @Schema(description = "Identificador del vehiculo asignado al viaje.", example = "8")
    private Long vehicleId;

    @Schema(description = "Identificador del terminal de origen.", example = "2")
    private Long originTerminalId;

    @Schema(description = "Identificador del terminal de destino.", example = "5")
    private Long destinationTerminalId;

    @Schema(description = "Identificador de la ruta asociada al viaje.", example = "11")
    private Long routeId;

    @Schema(description = "Estado actual del viaje.", example = "SCHEDULED")
    private String status;

    @Schema(description = "Fecha y hora programada para el viaje.", example = "2026-06-21T09:30:00")
    private LocalDateTime scheduledAt;

    @Schema(description = "Fecha y hora en que el viaje fue iniciado.", example = "2026-06-21T09:35:00")
    private LocalDateTime startedAt;

    @Schema(description = "Fecha y hora en que el viaje fue finalizado.", example = "2026-06-21T10:20:00")
    private LocalDateTime finishedAt;

    @Schema(description = "Indica si el viaje permanece activo.", example = "true")
    private Boolean active;

    @Schema(description = "Fecha y hora de creacion del viaje.", example = "2026-06-20T18:10:00")
    private LocalDateTime createdAt;
}
