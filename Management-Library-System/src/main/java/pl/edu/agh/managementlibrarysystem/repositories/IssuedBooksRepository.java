package pl.edu.agh.managementlibrarysystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.managementlibrarysystem.models.IssuedBook;
import pl.edu.agh.managementlibrarysystem.models.keys.IssuedBooksKey;
@Repository
public interface IssuedBooksRepository extends JpaRepository<IssuedBook, IssuedBooksKey> {
}
