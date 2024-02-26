package pl.edu.agh.managementlibrarysystem.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.model.Book;
import pl.edu.agh.managementlibrarysystem.model.IssuedBook;
import pl.edu.agh.managementlibrarysystem.repository.BookRepository;
import pl.edu.agh.managementlibrarysystem.repository.ReadBookRepository;
import pl.edu.agh.managementlibrarysystem.repository.SettingsRepository;

import java.util.Date;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class IssuedBooksRepositoryImpl {
    @PersistenceContext
    private final EntityManager entityManager;
    private final SettingsRepository settingsRepository;
    private final ReadBookRepository returnedBookRepository;
    private final BookRepository bookRepository;

    @Transactional
    @SuppressWarnings("unused")
    public void updateFee() {
        double feePerDay = Double.parseDouble(settingsRepository.getSettingValueWithGivenName("fee_per_day"));
        String query = "UPDATE issued_books ib SET ib.fee = (DATEDIFF(CURRENT_DATE, ib.issued_date) + 1) * " + feePerDay;

        entityManager.createNativeQuery(query).executeUpdate();
    }

    @Transactional
    @SuppressWarnings("unused")
    public void issueBook(String isbn, Long id, Integer days, boolean isTaken) {
        double feePerDay = Double.parseDouble(settingsRepository.getSettingValueWithGivenName("fee_per_day"));
        Book book = bookRepository.findByIsbn(isbn).orElse(null);
        int remainingBooks = book.getRemainingBooks();


        String queryUpdateRemainingBooks;

        if (remainingBooks == 1) {
            queryUpdateRemainingBooks = "UPDATE books b SET b.remaining_books = b.remaining_books - 1, b.availability = 'unavailable' WHERE b.id = " + book.getId();
        } else {
            queryUpdateRemainingBooks = "UPDATE books b SET b.remaining_books = b.remaining_books - 1 WHERE b.id = " + book.getId();
        }

        String query = "INSERT INTO issued_books (days, fee, user_id, book_id, issued_date, returned_date, is_taken) " +
                "VALUES (" + days + "," + feePerDay + " ," + id + ", (SELECT id FROM books WHERE isbn = '" + isbn + "'), CURRENT_DATE, NULL, " + isTaken + ")";

        entityManager.createNativeQuery(query).executeUpdate();
        entityManager.createNativeQuery(queryUpdateRemainingBooks).executeUpdate();
    }


    @Transactional
    @SuppressWarnings("unused")
    public void renewBook(Long bookId, Long userId, int numberOfDaysToRenew) {
        String query = "UPDATE issued_books ib SET ib.days = ib.days + ?1 WHERE ib.book_id = ?2 AND ib.user_id = ?3";
        entityManager.createNativeQuery(query)
                .setParameter(1, numberOfDaysToRenew)
                .setParameter(2, bookId)
                .setParameter(3, userId)
                .executeUpdate();
    }

    @Transactional
    @SuppressWarnings("unused")
    public boolean returnBook(Long bookId, Long userId) {
        Date issuedDate = (Date) entityManager.createNativeQuery("SELECT ib.issued_date FROM issued_books ib WHERE ib.book_id = ?1 AND ib.user_id = ?2")
                .setParameter(1, bookId)
                .setParameter(2, userId)
                .getSingleResult();

        int numberOfTotalIssuedDays = (int) ((new Date().getTime() - issuedDate.getTime()) / (1000 * 60 * 60 * 24)) + 1;
        double feePerDay = Double.parseDouble(settingsRepository.getSettingValueWithGivenName("fee_per_day"));

        Book book = bookRepository.findById(bookId).orElse(null);
        int remainingBooks = Objects.requireNonNull(book).getRemainingBooks();


        IssuedBook issuedBook = (IssuedBook) entityManager.createNativeQuery("SELECT * FROM issued_books ib WHERE ib.book_id = ?1 AND ib.user_id = ?2", IssuedBook.class)
                .setParameter(1, bookId)
                .setParameter(2, userId)
                .getSingleResult();

        if (!issuedBook.isTaken()) {
            return false;
        }

        String queryUpdateRemainingBooks;

        if (remainingBooks == 0) {
            queryUpdateRemainingBooks = "UPDATE books b SET b.remaining_books = b.remaining_books + 1, b.availability = 'available' WHERE b.id = " + book.getId();
        } else {
            queryUpdateRemainingBooks = "UPDATE books b SET b.remaining_books = b.remaining_books + 1 WHERE b.id = " + book.getId();
        }


        String insertQuery = "INSERT INTO read_books (user_id, book_id, returned_date, issued_date, days, fee) VALUES (?1, ?2, CURRENT_DATE, ?3, ?4, ?5)";


        entityManager.createNativeQuery(insertQuery)
                .setParameter(1, userId)
                .setParameter(2, bookId)
                .setParameter(3, issuedDate)
                .setParameter(4, numberOfTotalIssuedDays)
                .setParameter(5, numberOfTotalIssuedDays * feePerDay)
                .executeUpdate();

        entityManager.createNativeQuery(queryUpdateRemainingBooks).executeUpdate();

        return true;
    }

}
