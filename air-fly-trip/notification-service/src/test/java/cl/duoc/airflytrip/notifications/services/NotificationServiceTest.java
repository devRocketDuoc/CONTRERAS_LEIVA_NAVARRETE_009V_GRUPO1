package cl.duoc.airflytrip.notifications.services;

import cl.duoc.airflytrip.notifications.clients.AuthClient;
import cl.duoc.airflytrip.notifications.clients.response.UserResponse;
import cl.duoc.airflytrip.notifications.dtos.request.CreateNotificationRequest;
import cl.duoc.airflytrip.notifications.dtos.request.UpdateNotificationStatusRequest;
import cl.duoc.airflytrip.notifications.dtos.response.NotificationResponse;
import cl.duoc.airflytrip.notifications.exceptions.BadRequestException;
import cl.duoc.airflytrip.notifications.models.Notification;
import cl.duoc.airflytrip.notifications.repositories.NotificationRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void createShouldReturnPendingNotification() {

        CreateNotificationRequest request = new CreateNotificationRequest();
        request.setUserId(12L);
        request.setType("EMAIL");
        request.setTitle("Reserva confirmada");
        request.setMessage("Tu reserva fue confirmada.");

        when(authClient.findUserById(12L)).thenReturn(enabledUser(12L));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> {
            Notification notification = invocation.getArgument(0);
            notification.setId(15L);
            return notification;
        });

        NotificationResponse response = notificationService.create(request);

        assertNotNull(response);
        assertEquals(15L, response.getId());
        assertEquals("PENDING", response.getStatus());
        assertEquals("EMAIL", response.getType());
    }

    @Test
    void createShouldFailWhenUserIsDisabled() {

        CreateNotificationRequest request = new CreateNotificationRequest();
        request.setUserId(12L);
        request.setType("EMAIL");
        request.setTitle("Reserva confirmada");
        request.setMessage("Tu reserva fue confirmada.");

        when(authClient.findUserById(12L)).thenReturn(disabledUser(12L));

        Exception exception = null;

        try {
            notificationService.create(request);
        } catch (Exception ex) {
            exception = ex;
        }

        assertNotNull(exception);
        assertEquals(BadRequestException.class, exception.getClass());
        assertEquals("User is not enabled or does not exist with id: 12", exception.getMessage());
    }

    @Test
    void updateStatusShouldUpdateNotification() {

        UpdateNotificationStatusRequest request = new UpdateNotificationStatusRequest();
        request.setStatus("SENT");

        Notification notification = sampleNotification(15L, "PENDING");

        when(notificationRepository.findById(15L)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> invocation.getArgument(0));

        NotificationResponse response = notificationService.updateStatus(15L, request);

        assertNotNull(response);
        assertEquals("SENT", response.getStatus());
    }

    @Test
    void markAsReadShouldSetReadStatus() {

        Notification notification = sampleNotification(15L, "SENT");

        when(notificationRepository.findById(15L)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> invocation.getArgument(0));

        NotificationResponse response = notificationService.markAsRead(15L);

        assertNotNull(response);
        assertEquals("READ", response.getStatus());
    }

    private UserResponse enabledUser(Long id) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(id);
        userResponse.setEmail("user@example.com");
        userResponse.setEnabled(true);
        userResponse.setCreatedAt(LocalDateTime.of(2026, 1, 1, 8, 0));
        return userResponse;
    }

    private UserResponse disabledUser(Long id) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(id);
        userResponse.setEmail("user@example.com");
        userResponse.setEnabled(false);
        userResponse.setCreatedAt(LocalDateTime.of(2026, 1, 1, 8, 0));
        return userResponse;
    }

    private Notification sampleNotification(Long id, String status) {
        return Notification.builder()
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
