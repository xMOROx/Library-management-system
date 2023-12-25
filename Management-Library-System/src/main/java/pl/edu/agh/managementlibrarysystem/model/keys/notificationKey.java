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
public class notificationKey implements Serializable {
    @Serial
    private static final long serialVersionUID = 151313298798L;

    @Column(name = "book_id")
    private Long bookId;
    @Column(name = "user_id")
    private Long userId;

    public notificationKey() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        notificationKey that = (notificationKey) o;
        return Objects.equals(bookId, that.bookId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, userId);
    }
}
