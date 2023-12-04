package pl.edu.agh.managementlibrarysystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.managementlibrarysystem.model.ReadBook;
import pl.edu.agh.managementlibrarysystem.model.keys.readBooksKey;

public interface ReadBooksRepository extends JpaRepository<ReadBook, readBooksKey> {
}
