package pl.edu.agh.managementlibrarysystem.DTO;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReadBookAvailableToVoteDTO {
    String isbn;
    String title;
    String authors;
    String publisher;
    String genres;
    String returnedDate;
    int edition;
}
