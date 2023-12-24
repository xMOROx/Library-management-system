package pl.edu.agh.managementlibrarysystem.model;

import jakarta.persistence.*;
import lombok.*;
import pl.edu.agh.managementlibrarysystem.model.keys.IssuedBooksKey;

import java.sql.Date;

@Getter
@Setter
@Builder
@Entity(name = "issued_books")
@AllArgsConstructor
@NoArgsConstructor
public class IssuedBook {
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId("book_id")
    @JoinColumn(name = "book_id")
    private Book book;
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

}
