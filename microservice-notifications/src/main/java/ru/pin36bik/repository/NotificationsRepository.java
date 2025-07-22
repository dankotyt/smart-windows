package ru.pin36bik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pin36bik.model.Notifications;

import java.util.List;

public interface NotificationsRepository extends JpaRepository<Notifications, Long> {
    Notifications createNotification(Notifications notification);
    Notifications findNotificationById(Long id);
    List<Notifications> findNotificationsByUserId(Long userId);
    void deleteNotificationById(Long id);
}
