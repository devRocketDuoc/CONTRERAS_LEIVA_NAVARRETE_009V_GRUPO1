package cl.duoc.airflytrip.notifications.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateNotificationStatusRequest {

    @NotBlank(message = "Status is required")
    private String status;
}