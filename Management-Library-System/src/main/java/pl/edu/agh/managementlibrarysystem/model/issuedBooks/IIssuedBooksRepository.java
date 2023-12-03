package pl.edu.agh.managementlibrarysystem.model.issuedBooks;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IIssuedBooksRepository extends JpaRepository<Issued_books,Issued_books_key> {
}
