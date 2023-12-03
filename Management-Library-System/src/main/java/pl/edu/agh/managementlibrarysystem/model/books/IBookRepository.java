package pl.edu.agh.managementlibrarysystem.model.books;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBookRepository extends JpaRepository<Books,Long> {

}
