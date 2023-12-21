package pl.edu.agh.managementlibrarysystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Setter
@Getter
@Entity(name = "books")
@Builder
@AllArgsConstructor
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
    @OneToMany(mappedBy = "books", cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = IssuedBook.class)
    private Set<IssuedBook> issued_books;
    @OneToMany(mappedBy = "books", cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Notification.class)
    private Set<Notification> notification;

    @Column(name = "title", nullable = false, columnDefinition = "varchar(255) default 'Unknown'")
    private String title;
    @Column(name = "isbn", nullable = false, unique = true, length = 13, columnDefinition = "varchar(13)")
    private String isbn;
    @Column(name = "edition", columnDefinition = "int default 1")
    private int edition;
    @Column(name = "quantity", columnDefinition = "int default 1", nullable = false)
    private int quantity;
    @Column(name = "remaining_books", columnDefinition = "int default 1", nullable = false)
    private int remaining_books;
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

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Genre.class)
    @JoinTable(
            name = "books_genres",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = Publisher.class)
    @JoinColumn(name = "publisher_id", referencedColumnName = "id")
    private Publisher publisher;


    public Book() {
    }
}
