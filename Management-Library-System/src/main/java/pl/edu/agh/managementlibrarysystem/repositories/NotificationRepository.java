package pl.edu.agh.managementlibrarysystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.managementlibrarysystem.models.Notification;
import pl.edu.agh.managementlibrarysystem.models.keys.notificationKey;
@Repository
public interface NotificationRepository extends JpaRepository<Notification, notificationKey> {
}
