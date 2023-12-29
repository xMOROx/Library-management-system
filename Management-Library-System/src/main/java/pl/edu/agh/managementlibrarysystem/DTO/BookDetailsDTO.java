package pl.edu.agh.managementlibrarysystem.DTO;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BookDetailsDTO {
    String isbn;
    String title;
    String authors;
    String publisher;
    String genres;
    int edition;
    int quantity;
    int remainingBooks;
    String availability;
    String description;
    String cover;
    String image;
    String tableOfContent;
}
