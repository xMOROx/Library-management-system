package pl.edu.agh.managementlibrarysystem.DTO;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.StringProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReadBookAvailableToVoteDTO {
    StringProperty isbn;
    StringProperty title;
    StringProperty authors;
    StringProperty publisher;
    StringProperty genres;
    StringProperty returnedDate;
    LongProperty bookId;
    LongProperty userId;
    IntegerProperty edition;
}
