package cl.duoc.airflytrip.reservations.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Respuesta de error estandar del microservicio de reservas.")
public class ErrorResponse {

    @Schema(description = "Codigo HTTP asociado al error.", example = "400")
    private int status;

    @Schema(description = "Nombre corto del error HTTP.", example = "Bad Request")
    private String error;

    @Schema(description = "Detalle del error retornado por el servicio.", example = "Route does not match origin and destination terminals")
    private String message;

    @Schema(description = "Fecha y hora en que se genero el error.", example = "2026-06-21T10:20:00")
    private LocalDateTime timestamp;
}
