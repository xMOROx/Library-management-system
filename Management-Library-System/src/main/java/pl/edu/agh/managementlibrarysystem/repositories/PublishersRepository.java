package pl.edu.agh.managementlibrarysystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.managementlibrarysystem.model.Publisher;

public interface PublishersRepository extends JpaRepository<Publisher,Long> {
}
