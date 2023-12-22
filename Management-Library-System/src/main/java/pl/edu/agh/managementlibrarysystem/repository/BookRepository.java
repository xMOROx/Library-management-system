package pl.edu.agh.managementlibrarysystem.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.edu.agh.managementlibrarysystem.model.Book;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM books b WHERE b.isbn = ?1")
    Optional<String> findByIsbn(String isbn);
    @Transactional
    Optional<Book> saveNewBookWithGivenParams(Book book, String authorName, String authorLastname, String publisherName, String genreType);
}
