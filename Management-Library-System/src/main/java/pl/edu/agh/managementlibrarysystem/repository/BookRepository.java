package pl.edu.agh.managementlibrarysystem.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.edu.agh.managementlibrarysystem.model.Book;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface BookRepository extends JpaRepository<Book, Long> {
    String FIND_BY_ISBN = "SELECT b FROM books b WHERE b.isbn = ?1";
    String FIND_ALL_AVAILABLE_BOOKS = "SELECT b FROM books b WHERE b.availability = 'available'";
    String SUM_OF_ALL_AVAILABLE_BOOKS = "SELECT SUM(b.quantity) FROM books b WHERE b.availability = 'available'";
    String SUM_OF_ALL_BOOKS = "SELECT SUM(b.quantity) FROM books b";
    String SUM_OF_ALL_REMAINING_BOOKS = "SELECT SUM(b.remainingBooks) FROM books b WHERE b.remainingBooks > 0";
    String SUM_OF_ALL_AVAILABLE_REMAINING_BOOKS = "SELECT SUM(b.remainingBooks) FROM books b WHERE b.availability = 'available' AND b.remainingBooks > 0";
    String CHECK_IF_BOOK_IS_AVAILABLE = "SELECT b.availability FROM books b WHERE b.isbn = ?1";
    @Query(FIND_BY_ISBN)
    Optional<Book> findByIsbn(String isbn);

    @Query(SUM_OF_ALL_BOOKS)
    int sumOfAllBooks();

    @Query(SUM_OF_ALL_AVAILABLE_BOOKS)
    int sumOfAllAvailableBooks();

    @Query(SUM_OF_ALL_REMAINING_BOOKS)
    int sumOfAllRemainingBooks();

    @Query(SUM_OF_ALL_AVAILABLE_REMAINING_BOOKS)
    int sumOfAllAvailableRemainingBooks();

    @Query(FIND_ALL_AVAILABLE_BOOKS)
    List<Book> findAllAvailableBooks();

    @Query(CHECK_IF_BOOK_IS_AVAILABLE)
    String checkIfBookIsAvailable(String isbn);

    Optional<Book> saveNewBookWithGivenParams(Book book, String authorName, String authorLastname, String publisherName, String genreType);
}
