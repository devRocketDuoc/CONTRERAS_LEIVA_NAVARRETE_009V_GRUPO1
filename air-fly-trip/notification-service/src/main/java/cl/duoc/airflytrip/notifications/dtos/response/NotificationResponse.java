package cl.duoc.airflytrip.notifications.dtos.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponse {

    private Long id;

    private Long userId;

    private String type;

    private String title;

    private String message;

    private String status;

    private LocalDateTime createdAt;
}