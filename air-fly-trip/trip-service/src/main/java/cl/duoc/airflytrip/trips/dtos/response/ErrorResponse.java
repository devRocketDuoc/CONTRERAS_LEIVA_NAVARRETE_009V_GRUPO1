package cl.duoc.airflytrip.trips.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Respuesta de error estandar del microservicio de viajes.")
public class ErrorResponse {

    @Schema(description = "Codigo HTTP asociado al error.", example = "400")
    private int status;

    @Schema(description = "Nombre corto del error HTTP.", example = "Bad Request")
    private String error;

    @Schema(description = "Detalle del error retornado por el servicio.", example = "scheduledAt: Scheduled date must be now or in the future")
    private String message;

    @Schema(description = "Fecha y hora en que se genero el error.", example = "2026-06-20T11:20:00")
    private LocalDateTime timestamp;
}
