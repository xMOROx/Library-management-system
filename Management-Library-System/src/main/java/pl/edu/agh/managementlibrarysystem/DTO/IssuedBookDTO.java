package pl.edu.agh.managementlibrarysystem.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Builder
public class IssuedBookDTO {
    String issuedID;
    String bookISBN;
    String bookTitle;
    Long userID;
    String userFullName;
    String userEmail;
    String issuedDate;
    String returnedDate;
    Integer days;
    Double fee;
    String isTaken;
}
