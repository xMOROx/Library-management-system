package pl.edu.agh.managementlibrarysystem.model;

import jakarta.persistence.*;
import lombok.*;
import pl.edu.agh.managementlibrarysystem.model.util.Permission;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Builder
@Entity(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, targetEntity = ReadBook.class)
    private Set<ReadBook> readBooks = new HashSet<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, targetEntity = IssuedBook.class)
    private Set<IssuedBook> issuedBooks = new HashSet<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, targetEntity = Notification.class)
    private Set<Notification> notification = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "first_name", length = 100, nullable = false, columnDefinition = "VARCHAR(100)")
    private String firstname;
    @Column(name = "last_name", length = 100, nullable = false, columnDefinition = "VARCHAR(100)")
    private String lastname;
    @Column(name = "email", nullable = false, columnDefinition = "VARCHAR(255)")
    private String email;
    @Column(name = "password", nullable = false, columnDefinition = "VARCHAR(255)")
    private String password;
    @Enumerated(EnumType.STRING)
    private Permission permission;

    public User(String first_name, String last_name, String email, String password, Permission permission) {
        this.firstname = first_name;
        this.lastname = last_name;
        this.email = email;
        this.password = password;
        this.permission = permission;
    }
}