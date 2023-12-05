package pl.edu.agh.managementlibrarysystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.managementlibrarysystem.models.ReadBook;
import pl.edu.agh.managementlibrarysystem.models.keys.readBooksKey;
@Repository
public interface ReadBooksRepository extends JpaRepository<ReadBook, readBooksKey> {
}
