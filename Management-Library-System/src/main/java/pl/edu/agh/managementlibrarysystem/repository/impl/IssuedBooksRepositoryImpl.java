package pl.edu.agh.managementlibrarysystem.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.repository.ReturnedBookRepository;
import pl.edu.agh.managementlibrarysystem.repository.SettingsRepository;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class IssuedBooksRepositoryImpl {
    @PersistenceContext
    private final EntityManager entityManager;
    private final SettingsRepository settingsRepository;
    private final ReturnedBookRepository returnedBookRepository;

    @Transactional
    public void updateFee() {
        double feePerDay = Double.parseDouble(settingsRepository.getSettingValueWithGivenName("fee_per_day"));
        String query = "UPDATE issued_books ib SET ib.fee = (DATEDIFF(CURRENT_DATE, ib.issued_date) + 1) * " + feePerDay;

        entityManager.createNativeQuery(query).executeUpdate();
    }

    @Transactional
    public void issueBook(String isbn, Long id, Integer days) {
        double feePerDay = Double.parseDouble(settingsRepository.getSettingValueWithGivenName("fee_per_day"));

        String query = "INSERT INTO issued_books (days, fee, user_id, book_id, issued_date, returned_date) " +
                "VALUES (" + days + "," + feePerDay + " ," + id + ", (SELECT id FROM books WHERE isbn = '" + isbn + "'), CURRENT_DATE, NULL)";

        entityManager.createNativeQuery(query).executeUpdate();
    }

    @Transactional
    public void renewBook(Long bookId, Long userId, int numberOfDaysToRenew) {
        String query = "UPDATE issued_books ib SET ib.days = ib.days + ?1 WHERE ib.book_id = ?2 AND ib.user_id = ?3";
        entityManager.createNativeQuery(query)
                .setParameter(1, numberOfDaysToRenew)
                .setParameter(2, bookId)
                .setParameter(3, userId)
                .executeUpdate();
    }

    @Transactional
    public void returnBook(Long bookId, Long userId) {
        Date issuedDate = (Date) entityManager.createNativeQuery("SELECT ib.issued_date FROM issued_books ib WHERE ib.book_id = ?1 AND ib.user_id = ?2")
                .setParameter(1, bookId)
                .setParameter(2, userId)
                .getSingleResult();

        int numberOfTotalIssuedDays = (int) ((new Date().getTime() - issuedDate.getTime()) / (1000 * 60 * 60 * 24)) + 1;
        double feePerDay = Double.parseDouble(settingsRepository.getSettingValueWithGivenName("fee_per_day"));

        String insertQuery = "INSERT INTO returned_books (user_id, book_id, returned_date, issued_date, days, fee) VALUES (?1, ?2, CURRENT_DATE, ?3, ?4, ?5)";


        entityManager.createNativeQuery(insertQuery)
                .setParameter(1, userId)
                .setParameter(2, bookId)
                .setParameter(3, issuedDate)
                .setParameter(4, numberOfTotalIssuedDays)
                .setParameter(5, numberOfTotalIssuedDays * feePerDay)
                .executeUpdate();
    }

}
