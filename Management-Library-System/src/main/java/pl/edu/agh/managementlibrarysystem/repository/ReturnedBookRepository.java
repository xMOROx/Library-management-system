package pl.edu.agh.managementlibrarysystem.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.managementlibrarysystem.model.ReturnedBook;

@Repository
@Transactional
public interface ReturnedBookRepository extends JpaRepository<ReturnedBook, Long> {
}
