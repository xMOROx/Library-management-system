package pl.edu.agh.managementlibrarysystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Getter
@Setter
@Builder
@Entity(name = "read_books")
@AllArgsConstructor
@NoArgsConstructor
public class ReadBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "days", nullable = false, columnDefinition = "int")
    private int days;
    @Column(name = "fee", nullable = false, columnDefinition = "double")
    private double fee;
    @Column(name = "issued_date", nullable = false, columnDefinition = "DATE")
    private Date issuedDate;
    @Column(name = "returned_date", nullable = true, columnDefinition = "DATE")
    private Date returnedDate;

    @Column(name = "is_reviewed", nullable = false, columnDefinition = "boolean default false")
    private boolean isReviewed;

}
