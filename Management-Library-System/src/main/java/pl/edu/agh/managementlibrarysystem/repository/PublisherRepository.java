package pl.edu.agh.managementlibrarysystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.managementlibrarysystem.model.Publisher;
@Repository
public interface PublisherRepository extends JpaRepository<Publisher,Long> {
}
