package pl.edu.agh.managementlibrarysystem.model.genres;

import jakarta.persistence.*;
import pl.edu.agh.managementlibrarysystem.model.books.Books;

import java.util.Set;

@Entity
public class Genres {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    private String genre;

    @ManyToOne
    private Genres parent_Genre;
    @ManyToMany(mappedBy = "genres")
    private Set<Books> books;

    public Genres(String genre) {
        this.genre = genre;
    }

    public Genres() {
    }

    public long getId() {
        return id;
    }

    public String getGenre() {
        return genre;
    }

    public Genres getParent_Genre() {
        return parent_Genre;
    }


    public Set<Books> getBooks() {
        return books;
    }
}
