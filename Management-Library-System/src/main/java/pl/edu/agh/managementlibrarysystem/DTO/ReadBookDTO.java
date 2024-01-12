package pl.edu.agh.managementlibrarysystem.DTO;

import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Value
@Builder
public class ReadBookDTO {
    String isbn;
    String title;
    String authors;
    String publisher;
    String genres;
    Date issuedDate;
    Date returnedDate;
    int days;
    double fee;
    int edition;
}
