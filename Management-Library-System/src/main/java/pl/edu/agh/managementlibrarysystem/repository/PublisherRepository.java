package pl.edu.agh.managementlibrarysystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.agh.managementlibrarysystem.model.Publisher;

import java.util.Optional;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher,Long> {
    @Query("SELECT p from publishers p where p.name = ?1")
    Optional<Publisher> findByName(@Param("name") String name);
}
