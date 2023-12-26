package pl.edu.agh.managementlibrarysystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.managementlibrarysystem.model.ReadBook;
import pl.edu.agh.managementlibrarysystem.model.keys.ReadBooksKey;
@Repository
public interface ReadBooksRepository extends JpaRepository<ReadBook, ReadBooksKey> {
}
