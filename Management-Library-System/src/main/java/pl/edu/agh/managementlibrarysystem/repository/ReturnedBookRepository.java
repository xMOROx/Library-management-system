package pl.edu.agh.managementlibrarysystem.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.edu.agh.managementlibrarysystem.model.ReturnedBook;

import java.util.List;

@Repository
@Transactional
public interface ReturnedBookRepository extends JpaRepository<ReturnedBook, Long> {
    String FIND_ALL_READ_BOOK_AVAILABLE_TO_VOTE_BY_USER_ID = "SELECT rb FROM returned_books rb WHERE rb.user.id = ?1 AND rb.isReviewed = false";

    @Query(FIND_ALL_READ_BOOK_AVAILABLE_TO_VOTE_BY_USER_ID)
    List<ReturnedBook> findAllReadBookAvailableToVoteByUserId(Long id);
}
