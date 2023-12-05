package pl.edu.agh.managementlibrarysystem.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name")
    private String lastName;

    @ManyToMany(mappedBy = "authors", fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = Book.class)
    private Set<Book> books;

    public Author() {
    }

    public Author(String first_name, String last_name, Set<Book> books) {
        this.firstName = first_name;
        this.lastName = last_name;
        this.books = books;
    }

}
