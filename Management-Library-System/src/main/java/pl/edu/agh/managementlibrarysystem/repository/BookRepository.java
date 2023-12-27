package pl.edu.agh.managementlibrarysystem.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.edu.agh.managementlibrarysystem.DTO.BookDTO;
import pl.edu.agh.managementlibrarysystem.model.Book;

import java.util.Optional;

@Repository
@Transactional
public interface BookRepository extends JpaRepository<Book, Long> {
    String FIND_BY_ISBN = "SELECT b FROM books b WHERE b.isbn = ?1";
    String SUM_OF_ALL_BOOKS = "SELECT SUM(b.quantity) FROM books b";
    String SUM_OF_ALL_REMAINING_BOOKS = "SELECT SUM(b.remainingBooks) FROM books b";

    @Query(FIND_BY_ISBN)
    Optional<Book> findByIsbn(String isbn);
    @Query(SUM_OF_ALL_BOOKS)
    Integer sumOfAllBooks();
    @Query(SUM_OF_ALL_REMAINING_BOOKS)
    Integer sumOfAllRemainingBooks();
    Optional<Book> saveNewBookWithGivenParams(Book book, String authorName, String authorLastname, String publisherName, String genreType);
    public Optional<Book> updateBookWithGivenParams(Long bookId, BookDTO bookDTO, String authorName, String authorLastname, String publisherName, String genreType);
}
