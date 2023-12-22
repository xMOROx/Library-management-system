package pl.edu.agh.managementlibrarysystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.agh.managementlibrarysystem.model.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenresRepository extends JpaRepository<Genre,Long> {
    @Query("SELECT g from genres g where g.genre = ?1")
    Optional<Genre> findByType(@Param("genre") String genre);
    @Query("SELECT g from genres g where g.parentGenre is null")
    List<Genre> findAllMainGenres();
}
