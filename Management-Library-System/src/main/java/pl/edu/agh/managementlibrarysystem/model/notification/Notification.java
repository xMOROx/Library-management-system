package pl.edu.agh.managementlibrarysystem.model.notification;

import jakarta.persistence.*;
import pl.edu.agh.managementlibrarysystem.model.books.Books;
import pl.edu.agh.managementlibrarysystem.model.user.User;
import pl.edu.agh.managementlibrarysystem.model.util.Type;

import java.sql.Date;

@Entity
public class Notification {
    @EmbeddedId
    Notification_key id;
    @ManyToOne
    @MapsId("book_id")
    @JoinColumn(name="book_id")
    private Books books;
    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name="user_id")
    private User user;
    private Date sending_date;
    private Type type;
    private Boolean accepted;

    public Notification() {
    }

    public Notification(Books books, User user, Date sending_date, Type type, Boolean accepted) {
        this.books = books;
        this.user = user;
        this.sending_date = sending_date;
        this.type = type;
        this.accepted = accepted;
    }

    public Notification_key getId() {
        return id;
    }

    public Books getBooks() {
        return books;
    }

    public User getUser() {
        return user;
    }

    public Date getSending_date() {
        return sending_date;
    }

    public Type getType() {
        return type;
    }

    public Boolean getAccepted() {
        return accepted;
    }
}
