package cl.duoc.airflytrip.terminals.dtos.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Respuesta de error estandar del microservicio de terminales.")
public class ErrorResponse {

    @Schema(description = "Codigo HTTP asociado al error.", example = "400")
    private int status;

    @Schema(description = "Nombre corto del error HTTP.", example = "Bad Request")
    private String error;

    @Schema(description = "Detalle del error retornado por el servicio.", example = "name: Terminal name is required")
    private String message;

    @Schema(description = "Fecha y hora en que se genero el error.", example = "2026-06-20T11:40:00")
    private LocalDateTime timestamp;
}
