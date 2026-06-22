package cl.duoc.airflytrip.notifications.controllers;

import cl.duoc.airflytrip.notifications.dtos.request.CreateNotificationRequest;
import cl.duoc.airflytrip.notifications.dtos.request.UpdateNotificationStatusRequest;
import cl.duoc.airflytrip.notifications.dtos.response.NotificationResponse;
import cl.duoc.airflytrip.notifications.services.NotificationService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    @Test
    void createShouldReturnCreatedNotification() {

        CreateNotificationRequest request = new CreateNotificationRequest();
        request.setUserId(12L);
        request.setType("EMAIL");
        request.setTitle("Reserva confirmada");
        request.setMessage("Tu reserva fue confirmada.");

        NotificationResponse response = notificationResponse(15L, "PENDING");

        when(notificationService.create(any(CreateNotificationRequest.class))).thenReturn(response);

        ResponseEntity<NotificationResponse> result = notificationController.create(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(15L, result.getBody().getId());
    }

    @Test
    void findAllShouldReturnNotificationList() {

        when(notificationService.findAll()).thenReturn(List.of(
                notificationResponse(15L, "PENDING"),
                notificationResponse(16L, "SENT")
        ));

        ResponseEntity<List<NotificationResponse>> result = notificationController.findAll();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(2, result.getBody().size());
    }

    @Test
    void updateStatusShouldReturnUpdatedNotification() {

        UpdateNotificationStatusRequest request = new UpdateNotificationStatusRequest();
        request.setStatus("SENT");

        NotificationResponse response = notificationResponse(15L, "SENT");

        when(notificationService.updateStatus(eq(15L), any(UpdateNotificationStatusRequest.class))).thenReturn(response);

        ResponseEntity<NotificationResponse> result = notificationController.updateStatus(15L, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("SENT", result.getBody().getStatus());
    }

    @Test
    void markAsReadShouldReturnUpdatedNotification() {

        NotificationResponse response = notificationResponse(15L, "READ");

        when(notificationService.markAsRead(15L)).thenReturn(response);

        ResponseEntity<NotificationResponse> result = notificationController.markAsRead(15L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("READ", result.getBody().getStatus());
    }

    private NotificationResponse notificationResponse(Long id, String status) {
        return NotificationResponse.builder()
                .id(id)
                .userId(12L)
                .type("EMAIL")
                .title("Reserva confirmada")
                .message("Tu reserva fue confirmada.")
                .status(status)
                .createdAt(LocalDateTime.of(2026, 6, 21, 10, 30))
                .build();
    }
}
