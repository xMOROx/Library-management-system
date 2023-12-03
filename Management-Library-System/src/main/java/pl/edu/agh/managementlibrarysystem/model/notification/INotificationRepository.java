package pl.edu.agh.managementlibrarysystem.model.notification;

import org.springframework.data.jpa.repository.JpaRepository;

public interface INotificationRepository extends JpaRepository<Notification,Notification_key> {
}
