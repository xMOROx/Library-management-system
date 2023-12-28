package pl.edu.agh.managementlibrarysystem.model.keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class readBooksKey implements Serializable {
    @Serial
    private static final long serialVersionUID = 798123798312L;

    @Column(name="book_id")
    private Long bookId;
    @Column(name="user_id")
    private Long userId;

    public readBooksKey() {
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        readBooksKey that = (readBooksKey) o;
        return Objects.equals(bookId, that.bookId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, userId);
    }

}
