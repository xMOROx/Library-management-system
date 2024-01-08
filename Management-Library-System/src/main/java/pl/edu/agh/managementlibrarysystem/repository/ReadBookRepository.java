package pl.edu.agh.managementlibrarysystem.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.edu.agh.managementlibrarysystem.model.ReadBook;
import pl.edu.agh.managementlibrarysystem.model.keys.readBooksKey;

import java.util.List;

@Repository
@Transactional
public interface ReadBookRepository extends JpaRepository<ReadBook, readBooksKey> {
    String FIND_ALL_RATED_BOOKS_BY_USER_ID_n = "SELECT * FROM read_books WHERE user_id = ?1 AND rating IS NOT NULL or rating != -1";
    String CALCULATE_AVG_RATING_BY_BOOK_ID_n = "SELECT AVG(rating) FROM read_books WHERE book_id = ?1 AND rating IS NOT NULL";

    List<ReadBook> findAllByUserId(Long id);

    boolean reviewBook(long bookId, long userId, String review, double rating);

    boolean checkIfUserReviewedBook(long bookId, long userId);

    ReadBook findByUserIdAndBookId(Long userId, Long bookId);

    @Query(value = FIND_ALL_RATED_BOOKS_BY_USER_ID_n, nativeQuery = true)
    List<ReadBook> findAllRatedBooksByUserId(Long id);

    @Query(value = CALCULATE_AVG_RATING_BY_BOOK_ID_n, nativeQuery = true)
    double calculateAvgRatingByBookId(Long book);
}
