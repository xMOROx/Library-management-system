package pl.edu.agh.managementlibrarysystem.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.agh.managementlibrarysystem.model.Publisher;

import java.util.Optional;

@Repository
@Transactional
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    String FIND_BY_NAME = "SELECT p FROM publishers p WHERE p.name = ?1";

    @Query(FIND_BY_NAME)
    Optional<Publisher> findByName(@Param("name") String name);
}
