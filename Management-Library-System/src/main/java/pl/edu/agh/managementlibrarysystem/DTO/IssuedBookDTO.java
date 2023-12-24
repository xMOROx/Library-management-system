package pl.edu.agh.managementlibrarysystem.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IssuedBookDTO {
    private String issuedID;
    private String bookISBN;
    private String bookTitle;
    private Long userID;
    private String userFullName;
    private String issuedDate;
    private String returnedDate;
    private Integer days;
    private Double fee;
}
