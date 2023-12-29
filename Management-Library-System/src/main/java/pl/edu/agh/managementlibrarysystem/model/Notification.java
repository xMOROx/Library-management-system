package pl.edu.agh.managementlibrarysystem.model;

import jakarta.persistence.*;
import lombok.*;
import pl.edu.agh.managementlibrarysystem.model.util.Type;

import java.sql.Date;

@Getter
@Setter
@Builder
@Entity(name = "notifications")
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long notificationID;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "Id")
    private Book books;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "Id")
    private User user;
    @Column(name = "sending_date", nullable = false, columnDefinition = "DATE")
    private Date sendingDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type;
    @Column(name = "accepted", nullable = false, columnDefinition = "BOOLEAN default false")
    private Boolean accepted;


    public Notification(Book books, User user, Date sending_date, Type type, Boolean accepted) {
        this.books = books;
        this.user = user;
        this.sendingDate = sending_date;
        this.type = type;
        this.accepted = accepted;
    }

}
