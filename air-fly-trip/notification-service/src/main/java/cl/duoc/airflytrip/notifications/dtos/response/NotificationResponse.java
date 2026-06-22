package cl.duoc.airflytrip.notifications.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Respuesta con la informacion detallada de una notificacion.")
public class NotificationResponse {

    @Schema(description = "Identificador unico de la notificacion.", example = "15")
    private Long id;

    @Schema(description = "Identificador del usuario destinatario.", example = "12")
    private Long userId;

    @Schema(description = "Tipo de notificacion registrada.", example = "EMAIL")
    private String type;

    @Schema(description = "Titulo de la notificacion.", example = "Reserva confirmada")
    private String title;

    @Schema(description = "Mensaje detallado de la notificacion.", example = "Tu reserva para la ruta Santiago - Valparaiso fue confirmada.")
    private String message;

    @Schema(description = "Estado actual de la notificacion.", example = "PENDING")
    private String status;

    @Schema(description = "Fecha y hora de creacion de la notificacion.", example = "2026-06-21T10:30:00")
    private LocalDateTime createdAt;
}
