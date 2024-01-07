package pl.edu.agh.managementlibrarysystem.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.agh.managementlibrarysystem.model.Author;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface AuthorRepository extends JpaRepository<Author, Long> {
    String FIND_BY_NAME_AND_LASTNAME = "SELECT a FROM authors a WHERE a.firstname = ?1 AND a.lastname = ?2";
    String NUMBER_OF_ISSUES = "SELECT CONCAT(a.first_name,\" \",a.last_name) as name, COUNT(*) as number FROM books b " +
            "INNER JOIN issued_books ib ON b.id = ib.book_id INNER JOIN books_authors ba ON b.id = ba.book_id " +
            "INNER JOIN authors a ON ba.author_id = a.id GROUP BY a.id ORDER BY number DESC LIMIT ?1";
    @Query(FIND_BY_NAME_AND_LASTNAME)
    Optional<Author> findByNameAndLastname(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query(value = NUMBER_OF_ISSUES, nativeQuery = true)
    List<Object[]> getNumberOfIssues(Integer top);
}
