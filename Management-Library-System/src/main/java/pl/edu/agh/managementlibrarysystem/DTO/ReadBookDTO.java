package pl.edu.agh.managementlibrarysystem.DTO;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReadBookDTO {
    String isbn;
    String title;
    String authors;
    String genres;
    String review;
    double rating;
    int edition;
}
