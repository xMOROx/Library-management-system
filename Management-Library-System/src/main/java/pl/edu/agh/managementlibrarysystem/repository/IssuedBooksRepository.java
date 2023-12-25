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

    void updateFee();

    Optional<IssuedBook> findByUserIdAndBookIsbn(Long id, String isbn);

    void issueBook(String isbn, Long id, Integer days);


    void renewBook(Long bookId, Long userId, int numberOfDaysToRenew);

    void returnBook(Long bookId, Long userId);

    @Query(FIND_BY_BOOK_ID_AND_USER_ID)
    Optional<IssuedBook> findByBookIdAndUserId(Long bookId, Long userId);

    @Query(FIND_ALL_UP_TO_DATE)
    List<IssuedBook> findAllUpToDate();
}
