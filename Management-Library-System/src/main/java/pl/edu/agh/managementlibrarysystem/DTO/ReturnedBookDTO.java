package pl.edu.agh.managementlibrarysystem.DTO;

import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Value
@Builder
public class ReturnedBookDTO {
    String isbn;
    String title;
    String authors;
    String genres;
    int edition;
    int days;
    Date issued;
    Date returned;
}
