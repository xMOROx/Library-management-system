package pl.edu.agh.managementlibrarysystem.DTO;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.StringProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IssuedBookDTO {
    StringProperty issuedID;
    StringProperty bookISBN;
    StringProperty bookTitle;
    LongProperty userID;
    StringProperty userFullName;
    StringProperty userEmail;
    StringProperty issuedDate;
    StringProperty returnedDate;
    IntegerProperty days;
    DoubleProperty fee;
    StringProperty isTaken;
}
