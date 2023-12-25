package pl.edu.agh.managementlibrarysystem.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.agh.managementlibrarysystem.model.Author;

import java.util.Optional;

@Repository
@Transactional
public interface AuthorRepository extends JpaRepository<Author, Long> {
    String FIND_BY_NAME_AND_LASTNAME = "SELECT a FROM authors a WHERE a.firstname = ?1 AND a.lastname = ?2";

    @Query(FIND_BY_NAME_AND_LASTNAME)
    Optional<Author> findByNameAndLastname(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
