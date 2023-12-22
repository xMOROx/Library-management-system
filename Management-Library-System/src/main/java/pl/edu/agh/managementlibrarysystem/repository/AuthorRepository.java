package pl.edu.agh.managementlibrarysystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.agh.managementlibrarysystem.model.Author;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author,Long> {
    @Query("SELECT a FROM authors a WHERE a.firstName = ?1 AND a.lastName = ?2")
    Optional<Author> findByNameAndLastname(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
