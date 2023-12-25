package pl.edu.agh.managementlibrarysystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.managementlibrarysystem.model.keys.notificationKey;
import pl.edu.agh.managementlibrarysystem.model.util.Type;

import java.sql.Date;

@Getter
@Setter
@Entity(name = "notifications")
public class Notification {
    @EmbeddedId
    private notificationKey id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book books;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "sending_date", nullable = false, columnDefinition = "DATE")
    private Date sendingDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type;
    @Column(name = "accepted", nullable = false, columnDefinition = "BOOLEAN default false")
    private Boolean accepted;

    public Notification() {
    }

    public Notification(Book books, User user, Date sending_date, Type type, Boolean accepted) {
        this.books = books;
        this.user = user;
        this.sendingDate = sending_date;
        this.type = type;
        this.accepted = accepted;
    }

}
