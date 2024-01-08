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
public class ReadBookRepositoryImpl {
    @PersistenceContext
    private final EntityManager entityManager;

    public boolean reviewBook(long bookId, long userId, String review, double rating) {
        if (rating < 0 || rating > 5) {
            return false;
        }

        String insertQuery = "INSERT INTO read_books (book_id, user_id, review, rating) VALUES (?, ?, ?, ?)";
        String updateQuery = "UPDATE returned_books SET is_reviewed = true WHERE book_id = ? AND user_id = ?";

        this.entityManager.createNativeQuery(insertQuery)
                .setParameter(1, bookId)
                .setParameter(2, userId)
                .setParameter(3, review)
                .setParameter(4, BigDecimal.valueOf(rating).setScale(1, RoundingMode.HALF_UP).doubleValue())
                .executeUpdate();

        this.entityManager.createNativeQuery(updateQuery)
                .setParameter(1, bookId)
                .setParameter(2, userId)
                .executeUpdate();

        return true;
    }

    public boolean checkIfUserReviewedBook(long bookId, long userId) {
        String query = "SELECT COUNT(*) FROM read_books WHERE book_id = ? AND user_id = ?";

        return ((Number) this.entityManager.createNativeQuery(query)
                .setParameter(1, bookId)
                .setParameter(2, userId)
                .getSingleResult()).intValue() > 0;
    }
}
