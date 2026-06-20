package cl.duoc.airflytrip.chargingstations.dtos.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Respuesta de error estandar del microservicio de estaciones de carga.")
public class ErrorResponse {

    @Schema(description = "Codigo HTTP asociado al error.", example = "400")
    private int status;

    @Schema(description = "Nombre corto del error HTTP.", example = "Bad Request")
    private String error;

    @Schema(description = "Detalle del error retornado por el servicio.", example = "availableSlots: Available slots must be greater than or equal to 0")
    private String message;

    @Schema(description = "Fecha y hora en que se genero el error.", example = "2026-06-20T11:50:00")
    private LocalDateTime timestamp;
}
