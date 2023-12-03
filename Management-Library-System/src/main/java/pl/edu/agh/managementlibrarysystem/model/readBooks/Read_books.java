package pl.edu.agh.managementlibrarysystem.model.readBooks;

import jakarta.persistence.*;
import pl.edu.agh.managementlibrarysystem.model.books.Books;
import pl.edu.agh.managementlibrarysystem.model.user.User;

@Entity
public class Read_books {
    @EmbeddedId
    Read_books_key id;
    @ManyToOne
    @MapsId("book_id")
    @JoinColumn(name="book_id")
    private Books books;
    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name="user_id")
    private User user;
    private String review;
    private double rating;

    public Read_books() {
    }

    public Read_books(Books books, User user, String review, double rating) {
        this.books = books;
        this.user = user;
        this.review = review;
        this.rating = rating;
    }

    public Read_books_key getId() {
        return id;
    }

    public Books getBooks() {
        return books;
    }

    public User getUser() {
        return user;
    }

    public String getReview() {
        return review;
    }

    public double getRating() {
        return rating;
    }
}
