package pl.edu.agh.managementlibrarysystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity(name = "publishers")
@NoArgsConstructor
@AllArgsConstructor
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "name", nullable = false, columnDefinition = "varchar(255)")
    private String name;
    @OneToMany(mappedBy = "publisher", fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = Book.class)
    private Set<Book> books = new HashSet<>();

}
