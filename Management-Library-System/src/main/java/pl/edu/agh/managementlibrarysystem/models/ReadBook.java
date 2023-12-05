package pl.edu.agh.managementlibrarysystem.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.managementlibrarysystem.models.keys.readBooksKey;

@Getter
@Setter
@Entity(name = "read_books")
public class ReadBook {
    @EmbeddedId
    readBooksKey id;
    @ManyToOne
    @MapsId("book_id")
    @JoinColumn(name = "book_id")
    private Book books;
    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "review", length = 1000, columnDefinition = "TEXT")
    private String review;
    @Column(name = "rating", columnDefinition = "int default 1")
//    Min = 1, Max = 5
    private int rating;

    public ReadBook() {
    }

    public ReadBook(Book books, User user, String review, int rating) {
        this.books = books;
        this.user = user;
        this.review = review;
        this.rating = rating;
    }

}
