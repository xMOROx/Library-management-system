package pl.edu.agh.managementlibrarysystem.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.DTO.UserDTO;
import pl.edu.agh.managementlibrarysystem.mapper.abstraction.Mapper;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.model.util.Permission;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class UserMapper implements Mapper<User, UserDTO> {
    @Override
    public User mapToEntity(UserDTO userDTO) {
        String[] split = userDTO.getFullname().split(" ");
        String firstname = split[0];
        String lastname = split[1];

        return User.builder()
                .id(userDTO.getId())
                .firstname(firstname)
                .lastname(lastname)
                .email(userDTO.getEmail())
                .permission(Permission.valueOf(userDTO.getPermission()))
                .readBooks(new HashSet<>())
                .issuedBooks(new HashSet<>())
                .notification(new HashSet<>())
                .build();
    }

    @Override
    public UserDTO mapToDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .fullname(user.getFirstname() + " " + user.getLastname())
                .email(user.getEmail())
                .permission(user.getPermission().toString())
                .build();
    }
}
