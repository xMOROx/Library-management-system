package pl.edu.agh.managementlibrarysystem.model.user;

import jakarta.persistence.*;
import pl.edu.agh.managementlibrarysystem.model.issuedBooks.Issued_books;
import pl.edu.agh.managementlibrarysystem.model.notification.Notification;
import pl.edu.agh.managementlibrarysystem.model.readBooks.Read_books;
import pl.edu.agh.managementlibrarysystem.model.util.Permission;

import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private Permission permission;
    @OneToMany(mappedBy = "user")
    Set<Read_books> read_books;
    @OneToMany(mappedBy = "user")
    Set<Issued_books> issued_books;
    @OneToMany(mappedBy = "user")
    Set<Notification> notification;

    public User() {
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Permission getPermission() {
        return permission;
    }

    public Set<Read_books> getRead_books() {
        return read_books;
    }

    public Set<Issued_books> getIssued_books() {
        return issued_books;
    }

    public Set<Notification> getNotification() {
        return notification;
    }
}
