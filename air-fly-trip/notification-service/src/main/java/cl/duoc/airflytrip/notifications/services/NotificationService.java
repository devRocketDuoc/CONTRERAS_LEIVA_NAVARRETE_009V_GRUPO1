package cl.duoc.airflytrip.notifications.services;

import cl.duoc.airflytrip.notifications.clients.AuthClient;
import cl.duoc.airflytrip.notifications.clients.response.UserResponse;
import cl.duoc.airflytrip.notifications.dtos.request.CreateNotificationRequest;
import cl.duoc.airflytrip.notifications.dtos.request.UpdateNotificationStatusRequest;
import cl.duoc.airflytrip.notifications.dtos.response.NotificationResponse;
import cl.duoc.airflytrip.notifications.exceptions.BadRequestException;
import cl.duoc.airflytrip.notifications.exceptions.NotFoundException;
import cl.duoc.airflytrip.notifications.exceptions.RemoteServiceException;
import cl.duoc.airflytrip.notifications.models.Notification;
import cl.duoc.airflytrip.notifications.repositories.NotificationRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final String DEFAULT_STATUS = "PENDING";
    private static final String READ_STATUS = "READ";

    private final NotificationRepository notificationRepository;
    private final AuthClient authClient;

    public List<NotificationResponse> findAll() {
        return notificationRepository.findAll().stream().map(this::toResponse).toList();
    }

    public NotificationResponse findById(Long id) {
        return toResponse(findNotificationById(id));
    }

    public List<NotificationResponse> findByUserId(Long userId) {
        validateUserExists(userId);
        return notificationRepository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    public List<NotificationResponse> findByStatus(String status) {
        return notificationRepository.findByStatusIgnoreCase(status).stream().map(this::toResponse).toList();
    }

    public List<NotificationResponse> findByType(String type) {
        return notificationRepository.findByTypeIgnoreCase(type).stream().map(this::toResponse).toList();
    }

    public NotificationResponse create(CreateNotificationRequest request) {
        validateUserExists(request.getUserId());

        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .title(request.getTitle())
                .message(request.getMessage())
                .status(request.getStatus() != null ? request.getStatus() : DEFAULT_STATUS)
                .createdAt(LocalDateTime.now())
                .build();

        return toResponse(notificationRepository.save(notification));
    }

    public NotificationResponse updateStatus(Long id, UpdateNotificationStatusRequest request) {
        Notification notification = findNotificationById(id);
        notification.setStatus(request.getStatus());
        return toResponse(notificationRepository.save(notification));
    }

    public NotificationResponse markAsRead(Long id) {
        Notification notification = findNotificationById(id);
        notification.setStatus(READ_STATUS);
        return toResponse(notificationRepository.save(notification));
    }

    private Notification findNotificationById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Notification not found with id: " + id));
    }

    private void validateUserExists(Long userId) {
        try {
            UserResponse user = authClient.findUserById(userId);

            if (user == null || Boolean.FALSE.equals(user.getEnabled())) {
                throw new BadRequestException("User is not enabled or does not exist with id: " + userId);
            }
        } catch (FeignException.NotFound exception) {
            throw new BadRequestException("User not found with id: " + userId);
        } catch (FeignException exception) {
            throw new RemoteServiceException("Error communicating with auth-service");
        }
    }

    private NotificationResponse toResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .type(notification.getType())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .status(notification.getStatus())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}