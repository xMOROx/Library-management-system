package pl.edu.agh.managementlibrarysystem.DTO;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BookDTO {
    String isbn;
    String title;
    String author;
    String publisher;
    String mainGenre;
    Integer edition;
    Integer quantity;
    Integer remainingBooks;
    String availability;
    String description;
    String cover;
    String tableOfContent;
}
