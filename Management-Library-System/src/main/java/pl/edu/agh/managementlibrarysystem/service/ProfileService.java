package pl.edu.agh.managementlibrarysystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.managementlibrarysystem.DTO.IssuedBookDTO;
import pl.edu.agh.managementlibrarysystem.DTO.UserProfileDTO;
import pl.edu.agh.managementlibrarysystem.mapper.UserProfileMapper;
import pl.edu.agh.managementlibrarysystem.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserProfileMapper userProfileMapper;
    private final BookService bookService;
    public List<UserProfileDTO> getListOfUserProfileDTOofUser(User user){
        if(user!=null){

            List<IssuedBookDTO> issuedBooks = bookService.getIssuedBooksByUserId(user.getId());
            return issuedBooks.stream()
                    .map(userProfileMapper::mapToDto)
                    .toList();
        }
        return null;
    }
}
