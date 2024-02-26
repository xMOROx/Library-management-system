package pl.edu.agh.managementlibrarysystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.agh.managementlibrarysystem.model.Notification;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    String FIND_ALL_USER_BY_EMAIL = "SELECT n FROM notifications n WHERE n.user.email = :email";
    String SUM_ALL_BY_USER_EMAIL = "SELECT COUNT(n) FROM notifications n WHERE n.user.email = :email AND n.accepted = false";

    @Query(FIND_ALL_USER_BY_EMAIL)
    List<Notification> findAllByUserEmail(@Param("email") String email);

    @Query(SUM_ALL_BY_USER_EMAIL)
    Integer sumAllByUserEmail(@Param("email") String email);
}
