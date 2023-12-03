package pl.edu.agh.managementlibrarysystem.model.issuedBooks;

import jakarta.persistence.*;
import pl.edu.agh.managementlibrarysystem.model.books.Books;
import pl.edu.agh.managementlibrarysystem.model.user.User;

import java.sql.Date;

@Entity
public class Issued_books {
    @EmbeddedId
    private Issued_books_key id;
    @ManyToOne
    @MapsId("book_id")
    @JoinColumn(name="book_id")
    Books books;
    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name="user_id")
    User user;
    private int days;
    private double fee;
    private Date issued_date;
    private Date returned_date;

    public Issued_books(Books books, User user, int days, double fee, Date issued_date, Date returned_date) {
        this.books = books;
        this.user = user;
        this.days = days;
        this.fee = fee;
        this.issued_date = issued_date;
        this.returned_date = returned_date;
    }

    public Issued_books() {
    }

    public Issued_books_key getId() {
        return id;
    }

    public Books getBooks() {
        return books;
    }

    public User getUser() {
        return user;
    }

    public int getDays() {
        return days;
    }

    public double getFee() {
        return fee;
    }

    public Date getIssued_date() {
        return issued_date;
    }

    public Date getReturned_date() {
        return returned_date;
    }

}
