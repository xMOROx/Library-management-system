package pl.edu.agh.managementlibrarysystem.model.authors;

import jakarta.persistence.*;
import pl.edu.agh.managementlibrarysystem.model.books.Books;

import java.util.Set;

@Entity
public class Authors {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String first_name;
    private String last_name;
    @ManyToMany(mappedBy = "authors")
    private Set<Books> books;

    public Authors() {
    }

    public Authors(String first_name, String last_name, Set<Books> books) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.books = books;
    }

    public Long getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public Set<Books> getBooks() {
        return books;
    }
}
