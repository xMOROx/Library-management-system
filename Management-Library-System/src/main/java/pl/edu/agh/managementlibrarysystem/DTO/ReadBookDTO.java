package pl.edu.agh.managementlibrarysystem.DTO;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReadBookDTO {
    String review;
    String title;
    String isbn;
    String authors;
    String genres;
    int rating;
    int edition;
}
