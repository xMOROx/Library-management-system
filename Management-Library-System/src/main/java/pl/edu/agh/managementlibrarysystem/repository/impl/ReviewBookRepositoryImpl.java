package pl.edu.agh.managementlibrarysystem.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@Transactional
@RequiredArgsConstructor
public class ReviewBookRepositoryImpl {
    @PersistenceContext
    private final EntityManager entityManager;

    @SuppressWarnings("unused")
    public boolean reviewBook(long bookId, long userId, String review, double rating) {
        if (rating < 0 || rating > 5) {
            return false;
        }

        insertNewBookReview(bookId, userId, review, rating);
        updateReadBook(bookId, userId);

        return true;
    }

    @SuppressWarnings("unused")
    public boolean checkIfUserReviewedBook(long bookId, long userId) {
        String query = "SELECT COUNT(*) FROM review_books WHERE book_id = ? AND user_id = ?";

        return ((Number) this.entityManager.createNativeQuery(query)
                .setParameter(1, bookId)
                .setParameter(2, userId)
                .getSingleResult()).intValue() > 0;
    }

    private void insertNewBookReview(long bookId, long userId, String review, double rating) {
        String insertNewBookReview = "INSERT INTO review_books (book_id, user_id, review, rating) VALUES (?, ?, ?, ?)";

        this.entityManager.createNativeQuery(insertNewBookReview)
                .setParameter(1, bookId)
                .setParameter(2, userId)
                .setParameter(3, review)
                .setParameter(4, BigDecimal.valueOf(rating).setScale(1, RoundingMode.HALF_UP).doubleValue())
                .executeUpdate();
    }

    private void updateReadBook(long bookId, long userId) {
        String updateReadBook = "UPDATE read_books SET is_reviewed = true WHERE book_id = ? AND user_id = ?";

        this.entityManager.createNativeQuery(updateReadBook)
                .setParameter(1, bookId)
                .setParameter(2, userId)
                .executeUpdate();
    }
}
