package pl.edu.agh.managementlibrarysystem.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookDTO {
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String mainGenre;
    private Integer edition;
    private Integer quantity;
    private Integer remainingBooks;
    private String availability;
}
