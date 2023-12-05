package pl.edu.agh.managementlibrarysystem.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity(name = "publishers")
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "name", nullable = false, columnDefinition = "varchar(255)")
    private String name;
    @OneToMany(mappedBy = "publishers")
    private Set<Book> books;

    public Publisher(String name, Set<Book> books) {
        this.name = name;
        this.books = books;
    }

    public Publisher() {
    }

}
