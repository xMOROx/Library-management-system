package pl.edu.agh.managementlibrarysystem.model.keys;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class IssuedBooksKey implements Serializable {

    @Serial
    private final static long serialVersionUID = 4123432432437689L;
    @Column(name = "book_id")
    private Long bookId;
    @Column(name = "user_id")
    private Long userId;

    public IssuedBooksKey() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IssuedBooksKey that = (IssuedBooksKey) o;
        return Objects.equals(bookId, that.bookId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, userId);
    }
}
