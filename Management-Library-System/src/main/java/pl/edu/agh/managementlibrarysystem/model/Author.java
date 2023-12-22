package pl.edu.agh.managementlibrarysystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity(name = "authors")
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name")
    private String lastName;

    @ManyToMany(mappedBy = "authors", fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = Book.class)
    private Set<Book> books = new HashSet<>();
}
