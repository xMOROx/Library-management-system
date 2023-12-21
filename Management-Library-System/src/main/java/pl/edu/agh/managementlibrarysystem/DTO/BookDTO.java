package pl.edu.agh.managementlibrarysystem.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookDTO {
    private String title;
    private String isbn;
    private String publisher;
    private String author;
    private String mainGenre;
    private Integer edition;
    private Integer quantity;
    private Integer remainingBooks;
    private Boolean availability;
}
