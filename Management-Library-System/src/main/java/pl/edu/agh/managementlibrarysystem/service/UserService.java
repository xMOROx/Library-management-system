package pl.edu.agh.managementlibrarysystem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.managementlibrarysystem.DTO.UserDTO;
import pl.edu.agh.managementlibrarysystem.mapper.Mapper;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.model.util.Permission;
import pl.edu.agh.managementlibrarysystem.repository.IssuedBooksRepository;
import pl.edu.agh.managementlibrarysystem.repository.ReadBooksRepository;
import pl.edu.agh.managementlibrarysystem.repository.UserRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final IssuedBooksRepository issuedBooksRepository;
    private final ReadBooksRepository readBooksRepository;
    private final Mapper<User, UserDTO> userMapper;

    public Optional<User> addUser(String name, String surname, String email, String password) {

        User u = new User(name, surname, email, password, Permission.NORMAL_USER);
        return Optional.of(repository.save(u));
    }

    public UserDTO findById(long l) {
        return repository.findById(l).map(this.userMapper::mapToDto).orElse(null);
    }
}
