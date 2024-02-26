package pl.edu.agh.managementlibrarysystem.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.edu.agh.managementlibrarysystem.model.IssuedBook;
import pl.edu.agh.managementlibrarysystem.model.keys.IssuedBooksKey;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface IssuedBooksRepository extends JpaRepository<IssuedBook, IssuedBooksKey> {

    String FIND_BY_BOOK_ID_AND_USER_ID = "SELECT ib FROM issued_books ib WHERE ib.id.bookId = ?1 AND ib.id.userId = ?2 AND ib.returnedDate IS NULL";
    String FIND_ALL_UP_TO_DATE = "SELECT ib FROM issued_books ib WHERE ib.returnedDate IS NULL";
    String FIND_ALL_UP_TO_DATE_BY_USER_ID = "SELECT ib FROM issued_books ib WHERE ib.id.userId = ?1 AND ib.returnedDate IS NULL";
    String FIND_ISSUED_BOOK_BY_USER_ID_AND_BOOK_ISBN = "SELECT ib FROM issued_books ib WHERE ib.id.userId = ?1 AND ib.book.isbn = ?2 AND ib.returnedDate IS NULL";
    String SUM_ALL_FEES_BY_USER_ID = "SELECT SUM(fee) FROM issued_books ib WHERE ib.id.userId =?1 AND ib.returnedDate IS NULL";

    void deleteIssuedBookByBookIdAndUserId(Long bookId, Long userId);

    @Query(FIND_BY_BOOK_ID_AND_USER_ID)
    Optional<IssuedBook> findByBookIdAndUserId(Long bookId, Long userId);

    @Query(FIND_ISSUED_BOOK_BY_USER_ID_AND_BOOK_ISBN)
    Optional<IssuedBook> findIssuedBookByUserIdAndBookIsbn(Long id, String isbn);

    @Query(FIND_ALL_UP_TO_DATE_BY_USER_ID)
    List<IssuedBook> findAllUpToDateByUserId(Long id);

    @Query(FIND_ALL_UP_TO_DATE)
    List<IssuedBook> findAllUpToDate();

    @Query(SUM_ALL_FEES_BY_USER_ID)
    Double sumAllFeesByUserId(Long id);

    void updateFee();

    void issueBook(String isbn, Long id, Integer days, boolean isTaken);

    boolean returnBook(Long bookId, Long userId);

    void renewBook(Long bookId, Long userId, int numberOfDaysToRenew);


}
