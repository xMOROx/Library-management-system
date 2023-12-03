package pl.edu.agh.managementlibrarysystem.model.books;

import jakarta.persistence.*;
import pl.edu.agh.managementlibrarysystem.model.authors.Authors;
import pl.edu.agh.managementlibrarysystem.model.genres.Genres;
import pl.edu.agh.managementlibrarysystem.model.issuedBooks.Issued_books;
import pl.edu.agh.managementlibrarysystem.model.notification.Notification;
import pl.edu.agh.managementlibrarysystem.model.publishers.Publishers;
import pl.edu.agh.managementlibrarysystem.model.readBooks.Read_books;

import java.util.Set;

@Entity
public class Books {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private int author_id;
    private int publisher_id;
    private String title;
    private String isbn;
    private int edition;
    private int quantity;
    private int remaining_books;
    private char availability;
    private char cover;
    private String description;
    @ManyToMany
    @JoinTable(
            name="books_authors",
            joinColumns = @JoinColumn(name="book_id"),
            inverseJoinColumns = @JoinColumn(name="author_id")
    )
    Set<Authors> authors;
    @JoinTable(
            name="books_genres",
            joinColumns = @JoinColumn(name="book_id"),
            inverseJoinColumns = @JoinColumn(name="genre_id")
    )
    @ManyToMany
    Set<Genres> genres;
    @ManyToOne
    Publishers publishers;
    @OneToMany(mappedBy = "books")
    Set<Read_books> read_books;
    @OneToMany(mappedBy = "books")
    Set<Issued_books> issued_books;
    @OneToMany(mappedBy = "books")
    Set<Notification> notification;
    public Books(int author_id, int publisher_id, String title, String isbn, int edition, int quantity, int remaining_books, char availability, char cover, String description,
                 Set<Authors> authors, Set<Genres> genres, Publishers publishers, Set<Read_books> read_books, Set<Issued_books> issued_books, Set<Notification> notification) {
        this.author_id = author_id;
        this.publisher_id = publisher_id;
        this.title = title;
        this.isbn = isbn;
        this.edition = edition;
        this.quantity = quantity;
        this.remaining_books = remaining_books;
        this.availability = availability;
        this.cover = cover;
        this.description = description;
        this.authors = authors;
        this.genres = genres;
        this.publishers = publishers;
        this.read_books = read_books;
        this.issued_books = issued_books;
        this.notification = notification;
    }

    public Books() {
    }

    public Long getId() {
        return id;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public int getPublisher_id() {
        return publisher_id;
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getEdition() {
        return edition;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getRemaining_books() {
        return remaining_books;
    }

    public char getAvailability() {
        return availability;
    }

    public char getCover() {
        return cover;
    }

    public String getDescription() {
        return description;
    }

    public Set<Read_books> getRead_books() {
        return read_books;
    }

    public Set<Issued_books> getIssued_books() {
        return issued_books;
    }

    public Set<Notification> getNotification() {
        return notification;
    }
}
