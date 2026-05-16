package cl.duoc.airflytrip.notifications.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateNotificationRequest {

    @NotNull(message = "User id is required")
    private Long userId;

    @NotBlank(message = "Notification type is required")
    private String type;

    @NotBlank(message = "Notification title is required")
    private String title;

    @NotBlank(message = "Notification message is required")
    private String message;

    private String status;
}