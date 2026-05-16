package cl.duoc.airflytrip.notifications.repositories;


import cl.duoc.airflytrip.notifications.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserId(Long userId);

    List<Notification> findByStatusIgnoreCase(String status);

    List<Notification> findByTypeIgnoreCase(String type);
}