package cl.duoc.airflytrip.notifications.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos requeridos para crear una nueva notificacion en el sistema.")
public class CreateNotificationRequest {

    @NotNull(message = "User id is required")
    @Schema(description = "Identificador del usuario destinatario de la notificacion.", example = "12")
    private Long userId;

    @NotBlank(message = "Notification type is required")
    @Schema(description = "Tipo de notificacion que se registrara.", example = "EMAIL")
    private String type;

    @NotBlank(message = "Notification title is required")
    @Schema(description = "Titulo visible de la notificacion.", example = "Reserva confirmada")
    private String title;

    @NotBlank(message = "Notification message is required")
    @Schema(description = "Mensaje detallado de la notificacion.", example = "Tu reserva para la ruta Santiago - Valparaiso fue confirmada.")
    private String message;

    @Schema(description = "Estado inicial de la notificacion. Si no se envia, se asigna PENDING.", example = "PENDING")
    private String status;
}
