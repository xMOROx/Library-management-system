package pl.edu.agh.managementlibrarysystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.managementlibrarysystem.model.Notification;
import pl.edu.agh.managementlibrarysystem.model.keys.notificationKey;

public interface NotificationRepository extends JpaRepository<Notification, notificationKey> {
}
