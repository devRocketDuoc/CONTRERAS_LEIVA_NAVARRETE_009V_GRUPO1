package cl.duoc.airflytrip.auth.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Respuesta de error estandar del microservicio de autenticacion.")
public class ErrorResponse {

    @Schema(description = "Codigo HTTP asociado al error.", example = "409")
    private final int status;

    @Schema(description = "Nombre corto del error HTTP.", example = "Conflict")
    private final String error;

    @Schema(description = "Detalle del error retornado por el servicio.", example = "Email is already registered")
    private final String message;

    @Schema(description = "Fecha y hora en que se genero el error.", example = "2026-06-20T10:45:00")
    private final LocalDateTime timestamp;
}
