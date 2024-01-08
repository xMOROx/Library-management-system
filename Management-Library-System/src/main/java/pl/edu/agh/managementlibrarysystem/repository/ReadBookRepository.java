package pl.edu.agh.managementlibrarysystem.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.edu.agh.managementlibrarysystem.model.ReadBook;

import java.util.List;

@Repository
@Transactional
public interface ReadBookRepository extends JpaRepository<ReadBook, Long> {
    String FIND_ALL_READ_BOOK_AVAILABLE_TO_VOTE_BY_USER_ID = "SELECT rb FROM read_books rb WHERE rb.user.id = ?1 AND rb.isReviewed = false";
    String COUNT_READ_BOOKS = "SELECT COUNT(*) FROM read_books rb INNER JOIN users u ON u.id = rb.user_id WHERE u.id = ?1";
    String SUM_USER_FEES = "SELECT SUM(rb.fee) FROM read_books rb INNER JOIN users u ON u.id = rb.user_id WHERE u.id = ?1";
    @Query(FIND_ALL_READ_BOOK_AVAILABLE_TO_VOTE_BY_USER_ID)
    List<ReadBook> findAllReadBookAvailableToVoteByUserId(Long id);
    @Query(value = COUNT_READ_BOOKS, nativeQuery = true)
    Integer getCountOfReadBooks(Long id);
    @Query(value = SUM_USER_FEES, nativeQuery = true)
    Double getSumOfUserFees(Long id);
    List<ReadBook> findAllByUserId(Long id);

}
