package pl.edu.agh.managementlibrarysystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.agh.managementlibrarysystem.model.keys.reviewBooksKey;

@Getter
@Setter
@Entity(name = "review_books")
@NoArgsConstructor
@AllArgsConstructor
public class ReviewBook {
    @EmbeddedId
    private reviewBooksKey id;
    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "review", length = 1000, columnDefinition = "TEXT")
    private String review;
    @Column(name = "rating", columnDefinition = "double(3,2) default -1")
//    Min = 0, Max = 5
    private double rating;
}
