package pl.edu.agh.managementlibrarysystem.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.DTO.IssuedBookDTO;
import pl.edu.agh.managementlibrarysystem.DTO.UserProfileDTO;
import pl.edu.agh.managementlibrarysystem.mapper.abstraction.Mapper;
import pl.edu.agh.managementlibrarysystem.mapper.abstraction.OneWayMapper;

@Component
@RequiredArgsConstructor
public class UserProfileMapper implements OneWayMapper<IssuedBookDTO, UserProfileDTO> {

    @Override
    public UserProfileDTO map(IssuedBookDTO object) {
        return UserProfileDTO.builder()
                .bookTitle(object.getBookTitle().getValue())
                .borrowDate(object.getIssuedDate().getValue())
                .dueFees(object.getFee().getValue())
                .dueDate(object.getReturnedDate().getValue())
                .currentlyBorrowed(object.getIsTaken().getValue())
                .build();
    }
}
