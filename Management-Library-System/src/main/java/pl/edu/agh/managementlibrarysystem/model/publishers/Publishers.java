package pl.edu.agh.managementlibrarysystem.model.publishers;

import jakarta.persistence.*;
import pl.edu.agh.managementlibrarysystem.model.books.Books;

import java.util.Set;

@Entity
public class Publishers {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "publishers")
    private Set<Books> books;

    public Publishers(String name, Set<Books> books) {
        this.name = name;
        this.books = books;
    }

    public Publishers() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Books> getBooks() {
        return books;
    }
}
