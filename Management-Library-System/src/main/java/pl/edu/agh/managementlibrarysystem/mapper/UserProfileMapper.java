package pl.edu.agh.managementlibrarysystem.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.DTO.IssuedBookDTO;
import pl.edu.agh.managementlibrarysystem.DTO.UserProfileDTO;

@Component
@RequiredArgsConstructor
public class UserProfileMapper implements Mapper<IssuedBookDTO, UserProfileDTO>{

    @Override
    public IssuedBookDTO mapToEntity(UserProfileDTO object) {
        return null;
    }

    @Override
    public UserProfileDTO mapToDto(IssuedBookDTO object) {
        return UserProfileDTO.builder()
                .bookTitle(object.getBookTitle())
                .borrowDate(object.getIssuedDate())
                .dueFees(object.getFee())
                .dueDate(object.getReturnedDate())
                .currentlyBorrowed(object.getIsTaken())
                .build();
    }
}
