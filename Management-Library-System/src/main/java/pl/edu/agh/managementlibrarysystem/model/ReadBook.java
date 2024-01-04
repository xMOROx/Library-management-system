package pl.edu.agh.managementlibrarysystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.agh.managementlibrarysystem.model.keys.readBooksKey;

@Getter
@Setter
@Entity(name = "read_books")
@NoArgsConstructor
@AllArgsConstructor
public class ReadBook {
    @EmbeddedId
    private readBooksKey id;
    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book books;
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "review", length = 1000, columnDefinition = "TEXT")
    private String review;
    @Column(name = "rating", columnDefinition = "int default 1")
//    Min = 1, Max = 5
    private int rating;
}
