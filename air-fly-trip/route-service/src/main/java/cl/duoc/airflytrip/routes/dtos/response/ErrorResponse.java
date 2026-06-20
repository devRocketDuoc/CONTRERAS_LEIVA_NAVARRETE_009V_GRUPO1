package cl.duoc.airflytrip.routes.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Respuesta de error estandar del microservicio de rutas.")
public class ErrorResponse {

    @Schema(description = "Codigo HTTP asociado al error.", example = "400")
    private int status;

    @Schema(description = "Nombre corto del error HTTP.", example = "Bad Request")
    private String error;

    @Schema(description = "Detalle del error retornado por el servicio.", example = "distanceKm: Distance must be greater than 0")
    private String message;

    @Schema(description = "Fecha y hora en que se genero el error.", example = "2026-06-20T12:00:00")
    private LocalDateTime timestamp;
}
