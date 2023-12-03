package pl.edu.agh.managementlibrarysystem.model.readBooks;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class Read_books_key implements Serializable {
    @Column(name="book_id")
    Long book_id;
    @Column(name="user_id")
    Long user_id;

    public Read_books_key() {
    }
    public Long getBook_id() {
        return book_id;
    }

    public void setBook_id(Long book_id) {
        this.book_id = book_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Read_books_key that = (Read_books_key) o;
        return Objects.equals(book_id, that.book_id) && Objects.equals(user_id, that.user_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(book_id, user_id);
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }
}
