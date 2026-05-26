package cl.duoc.airflytrip.notifications.controllers;

import cl.duoc.airflytrip.notifications.dtos.request.CreateNotificationRequest;
import cl.duoc.airflytrip.notifications.dtos.request.UpdateNotificationStatusRequest;
import cl.duoc.airflytrip.notifications.dtos.response.NotificationResponse;
import cl.duoc.airflytrip.notifications.services.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    public ResponseEntity<List<NotificationResponse>> findAll() {
        return ResponseEntity.ok(notificationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.findById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>> findByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.findByUserId(userId));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    public ResponseEntity<List<NotificationResponse>> findByStatus(@PathVariable String status) {
        return ResponseEntity.ok(notificationService.findByStatus(status));
    }

    @GetMapping("/type/{type}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    public ResponseEntity<List<NotificationResponse>> findByType(@PathVariable String type) {
        return ResponseEntity.ok(notificationService.findByType(type));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    public ResponseEntity<NotificationResponse> create(@Valid @RequestBody CreateNotificationRequest request) {
        NotificationResponse response = notificationService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    public ResponseEntity<NotificationResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateNotificationStatusRequest request
    ) {
        return ResponseEntity.ok(notificationService.updateStatus(id, request));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<NotificationResponse> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }
}