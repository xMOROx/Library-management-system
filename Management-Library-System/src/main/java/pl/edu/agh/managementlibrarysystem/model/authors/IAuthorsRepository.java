package pl.edu.agh.managementlibrarysystem.model.authors;

import org.springframework.data.jpa.repository.JpaRepository;
public interface IAuthorsRepository extends JpaRepository<Authors,Long> {
}
