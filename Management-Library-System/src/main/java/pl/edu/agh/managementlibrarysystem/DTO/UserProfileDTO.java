package pl.edu.agh.managementlibrarysystem.DTO;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserProfileDTO {
    String bookTitle;
    String borrowDate;
    String dueDate;
    Double dueFees;
    String currentlyBorrowed;
}
