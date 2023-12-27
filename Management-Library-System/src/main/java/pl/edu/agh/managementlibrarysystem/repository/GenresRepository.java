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

    @Query(FIND_BY_TYPE)
    Optional<Genre> findByType(@Param("genre") String genre);
    @Query(FIND_ALL_MAIN_GENRES)
    List<Genre> findAllMainGenres();
}
