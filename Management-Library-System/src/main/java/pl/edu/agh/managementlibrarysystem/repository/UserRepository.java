package pl.edu.agh.managementlibrarysystem.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.edu.agh.managementlibrarysystem.DTO.UserStatsDTO;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.model.util.Permission;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    String NUMBER_OF_USERS = "SELECT COUNT(*) FROM users WHERE users.permission = \"NORMAL_USER\"";
    Optional<User> findByEmail(String email);

    @Modifying
    @Query("UPDATE users u SET u.firstname = ?2, u.lastname = ?3, u.email=?4, u.password=?5, u.permission=?6 WHERE u.id=?1")
    int updateUser(Long id, String firstname, String lastname, String email, String password, Permission permission);

    List<UserStatsDTO> getALLUserStats();
    @Query(value = NUMBER_OF_USERS, nativeQuery = true)
    Integer getNumberOfNormalUsers();
}
