package pl.edu.agh.managementlibrarysystem.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.edu.agh.managementlibrarysystem.model.ReviewBook;
import pl.edu.agh.managementlibrarysystem.model.keys.reviewBooksKey;

import java.util.List;

@Repository
@Transactional
public interface ReviewBookRepository extends JpaRepository<ReviewBook, reviewBooksKey> {
    String FIND_ALL_RATED_BOOKS_BY_USER_ID_n = "SELECT * FROM review_books WHERE user_id = ?1 AND rating IS NOT NULL or rating != -1";
    String CALCULATE_AVG_RATING_BY_BOOK_ID_n = "SELECT AVG(rating) FROM review_books WHERE book_id = ?1 AND rating IS NOT NULL";
    String COUNT_REVIEWS_GIVEN = "SELECT COUNT(rb.review) FROM review_books rb INNER JOIN users u ON u.id = rb.user_id WHERE u.id = ?1";
    String COUNT_RATINGS_GIVEN = "SELECT COUNT(rb.rating) FROM review_books rb INNER JOIN users u ON u.id = rb.user_id WHERE u.id = ?1";
    String AVG_RATINGS_GIVEN = "SELECT AVG(rb.rating) FROM review_books rb INNER JOIN users u ON u.id = rb.user_id WHERE u.id = ?1";
    List<ReviewBook> findAllByUserId(Long id);

    boolean reviewBook(long bookId, long userId, String review, double rating);

    boolean checkIfUserReviewedBook(long bookId, long userId);

    ReviewBook findByUserIdAndBookId(Long userId, Long bookId);

    @Query(value = FIND_ALL_RATED_BOOKS_BY_USER_ID_n, nativeQuery = true)
    List<ReviewBook> findAllRatedBooksByUserId(Long id);

    @Query(value = CALCULATE_AVG_RATING_BY_BOOK_ID_n, nativeQuery = true)
    double calculateAvgRatingByBookId(Long book);

    @Query(value = COUNT_REVIEWS_GIVEN, nativeQuery = true)
    Integer getCountOfReviewsGiven(Long id);
    @Query(value = COUNT_RATINGS_GIVEN, nativeQuery = true)
    Integer getCountOfRatingsGiven(Long id);
    @Query(value = AVG_RATINGS_GIVEN, nativeQuery = true)
    Double getAvgOfRatingsGiven(Long id);
}
