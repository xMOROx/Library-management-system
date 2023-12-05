package pl.edu.agh.managementlibrarysystem.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.managementlibrarysystem.models.keys.IssuedBooksKey;

import java.sql.Date;

@Getter
@Setter
@Entity(name = "issued_books")
public class IssuedBook {
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId("book_id")
    @JoinColumn(name = "book_id")
    private Book books;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId("user_id")
    @JoinColumn(name = "user_id")
    private User user;
    @EmbeddedId
    private IssuedBooksKey id;

    @Column(name = "days", nullable = false, columnDefinition = "int default 1")
    private int days;
    @Column(name = "fee", nullable = false, columnDefinition = "double default 10.0")
    private double fee;
    @Column(name = "issued_date", nullable = false, columnDefinition = "DATE default CURRENT_DATE")
    private Date issuedDate;
    @Column(name = "returned_date", nullable = false, columnDefinition = "DATE")
    private Date returnedDate;

    public IssuedBook(Book books, User user, int days, double fee, Date issued_date, Date returned_date) {
        this.books = books;
        this.user = user;
        this.days = days;
        this.fee = fee;
        this.issuedDate = issued_date;
        this.returnedDate = returned_date;
    }

    public IssuedBook() {
    }

}
