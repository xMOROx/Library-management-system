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
public class IssuedBooksKey implements Serializable {

    @Serial
    private final static long serialVersionUID = 4123432432437689L;
    @Column(name = "book_id")
    private Long book_id;
    @Column(name = "user_id")
    private Long user_id;

    public IssuedBooksKey() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IssuedBooksKey that = (IssuedBooksKey) o;
        return Objects.equals(book_id, that.book_id) && Objects.equals(user_id, that.user_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(book_id, user_id);
    }
}
