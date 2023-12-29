package pl.edu.agh.managementlibrarysystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.agh.managementlibrarysystem.model.Notification;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM notifications n WHERE n.user.email = :email")
    List<Notification> findALLByUserEmail(@Param("email") String email);
    @Query("SELECT COUNT(n) FROM notifications n WHERE n.user.email = :email AND n.accepted = false")
    Integer sumAllByUserEmail(@Param("email") String email);
}
