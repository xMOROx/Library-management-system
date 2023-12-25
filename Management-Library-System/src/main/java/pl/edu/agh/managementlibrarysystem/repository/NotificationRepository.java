package pl.edu.agh.managementlibrarysystem.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.managementlibrarysystem.model.Notification;
import pl.edu.agh.managementlibrarysystem.model.keys.notificationKey;

@Repository
@Transactional
public interface NotificationRepository extends JpaRepository<Notification, notificationKey> {
}