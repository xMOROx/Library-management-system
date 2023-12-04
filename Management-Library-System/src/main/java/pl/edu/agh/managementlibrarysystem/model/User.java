package pl.edu.agh.managementlibrarysystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.managementlibrarysystem.model.util.Permission;

import java.util.Set;

@Getter
@Setter
@Entity(name = "users")
public class User {
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, targetEntity = ReadBook.class)
    Set<ReadBook> read_books;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, targetEntity = IssuedBook.class)
    Set<IssuedBook> issued_books;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, targetEntity = Notification.class)
    Set<Notification> notification;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "first_name", length = 100, nullable = false, columnDefinition = "VARCHAR(100)")
    private String firstName;
    @Column(name = "last_name", length = 100, nullable = false, columnDefinition = "VARCHAR(100)")
    private String lastName;
    @Column(name = "email", nullable = false, columnDefinition = "VARCHAR(255)")
    private String email;
    @Column(name = "password", nullable = false, columnDefinition = "VARCHAR(255)")
    private String password;
    @Enumerated(EnumType.STRING)
    private Permission permission;

    public User() {
    }

    public User(String first_name, String last_name, String email, String password, Permission permission) {
        this.firstName = first_name;
        this.lastName = last_name;
        this.email = email;
        this.password = password;
        this.permission = permission;
    }

}