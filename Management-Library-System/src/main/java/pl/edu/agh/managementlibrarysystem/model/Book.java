package pl.edu.agh.managementlibrarysystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Setter
@Getter
@Builder
@Entity(name = "books")
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Author.class)
    @JoinTable(
            name = "books_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors;
    @OneToMany(mappedBy = "books", cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = ReadBook.class)
    private Set<ReadBook> read_books;
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = IssuedBook.class)
    private Set<IssuedBook> issued_books;
    @OneToMany(mappedBy = "books", cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Notification.class)
    private Set<Notification> notification;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Genre.class)
    @JoinTable(
            name = "books_genres",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres;

    @Column(name = "title", nullable = false, columnDefinition = "varchar(255) default 'Unknown'")
    private String title;
    @Column(name = "isbn", nullable = false, unique = true, length = 255, columnDefinition = "varchar(255)")
    private String isbn;
    @Column(name = "edition", columnDefinition = "int default 1")
    private int edition;
    @Column(name = "quantity", columnDefinition = "int default 1", nullable = false)
    private int quantity;
    @Column(name = "remaining_books", columnDefinition = "int default 1", nullable = false)
    private int remainingBooks;
    @Column(name = "availability", columnDefinition = "varchar(255) default 'available'", nullable = false)
    private String availability;
    @Column(name = "cover", columnDefinition = "varchar(255) default 'hard'")
    private String cover;
    @Column(name = "description", columnDefinition = "varchar(255) default 'No description'")
    private String description;
    @Column(name = "image", columnDefinition = "varchar(255) default 'No image'")
    private String image;
    @Column(name = "table_of_content", columnDefinition = "varchar(255)")
    private String tableOfContent;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = Publisher.class)
    @JoinColumn(name = "publisher_id", referencedColumnName = "id")
    private Publisher publisher;

}
