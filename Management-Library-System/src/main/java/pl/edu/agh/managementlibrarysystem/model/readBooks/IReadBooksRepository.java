package pl.edu.agh.managementlibrarysystem.model.readBooks;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IReadBooksRepository extends JpaRepository<Read_books,Read_books_key> {
}
