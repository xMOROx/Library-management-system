package pl.edu.agh.managementlibrarysystem.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.agh.managementlibrarysystem.model.Genre;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface GenresRepository extends JpaRepository<Genre, Long> {
    String FIND_BY_TYPE = "SELECT g FROM genres g WHERE g.genre = ?1";
    String FIND_ALL_MAIN_GENRES = "SELECT g FROM genres g WHERE g.parentGenre is null";
    String NUMBER_OF_ISSUES = "SELECT g.genre as name, COUNT(*) as number FROM books b INNER JOIN issued_books ib " +
            "ON b.id = ib.book_id INNER JOIN books_genres bg ON b.id = bg.book_id " +
            "INNER JOIN genres g ON bg.genre_id = g.id GROUP BY g.id ORDER BY number DESC LIMIT ?1";
    @Query(FIND_BY_TYPE)
    Optional<Genre> findByType(@Param("genre") String genre);

    @Query(FIND_ALL_MAIN_GENRES)
    List<Genre> findAllMainGenres();

    @Query(value=NUMBER_OF_ISSUES,nativeQuery = true)
    List<Object[]> getNumberOfIssues(Integer top);
}
