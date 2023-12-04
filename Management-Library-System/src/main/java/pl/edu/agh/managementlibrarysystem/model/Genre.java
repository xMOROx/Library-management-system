package pl.edu.agh.managementlibrarysystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity(name = "genres")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "genre", nullable = false, columnDefinition = "varchar(255)")
    private String genre;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = Genre.class)
    private Genre parentGenre;

    @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = Book.class)
    private Set<Book> books;

    public Genre(String genre) {
        this.genre = genre;
    }

    public Genre() {
    }


}
