package pl.edu.agh.managementlibrarysystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.managementlibrarysystem.model.User;

public interface UserRepository extends JpaRepository<User,Long> {
}
