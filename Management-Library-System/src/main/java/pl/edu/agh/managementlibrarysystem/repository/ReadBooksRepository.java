package pl.edu.agh.managementlibrarysystem.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.edu.agh.managementlibrarysystem.model.ReadBook;
import pl.edu.agh.managementlibrarysystem.model.keys.readBooksKey;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ReadBooksRepository extends JpaRepository<ReadBook, readBooksKey> {

    List<ReadBook> findAllByUserId(Long id);
}
