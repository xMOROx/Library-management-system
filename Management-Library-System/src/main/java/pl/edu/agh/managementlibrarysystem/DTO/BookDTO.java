package pl.edu.agh.managementlibrarysystem.DTO;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Builder
public class BookDTO {
    private StringProperty isbn;
    private StringProperty title;
    private StringProperty authors;
    private StringProperty publisher;
    private StringProperty mainGenre;
    private IntegerProperty edition;
    private IntegerProperty quantity;
    private IntegerProperty remainingBooks;
    private StringProperty availability;
    private StringProperty description;
    private StringProperty cover;
    private StringProperty image;
    private StringProperty tableOfContent;
}
