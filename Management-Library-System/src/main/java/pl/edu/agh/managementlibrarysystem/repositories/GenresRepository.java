package pl.edu.agh.managementlibrarysystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.managementlibrarysystem.model.Genre;

public interface GenresRepository extends JpaRepository<Genre,Long> {
}
