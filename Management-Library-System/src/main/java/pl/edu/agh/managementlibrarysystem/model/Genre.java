package pl.edu.agh.managementlibrarysystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity(name = "genres")
@NoArgsConstructor
@AllArgsConstructor
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "genre", nullable = false, columnDefinition = "varchar(255)")
    private String genre;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = Genre.class)
    private Genre parentGenre;

    @OneToMany(mappedBy = "parentGenre", fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = Genre.class)
    private Set<Genre> subGenres = new HashSet<>();

    @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = Book.class)
    private Set<Book> books = new HashSet<>();

    public Genre addSubGenre(Genre subGenre) {
        this.subGenres.add(subGenre);
        subGenre.setParentGenre(this);
        return subGenre;
    }
}
