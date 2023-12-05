package pl.edu.agh.managementlibrarysystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.managementlibrarysystem.models.Publisher;
@Repository
public interface PublishersRepository extends JpaRepository<Publisher,Long> {
}
