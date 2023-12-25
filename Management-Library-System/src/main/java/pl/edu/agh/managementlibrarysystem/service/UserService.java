package pl.edu.agh.managementlibrarysystem.service;

import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.agh.managementlibrarysystem.DTO.UserDTO;
import pl.edu.agh.managementlibrarysystem.mapper.Mapper;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.model.util.Permission;
import pl.edu.agh.managementlibrarysystem.repository.IssuedBooksRepository;
import pl.edu.agh.managementlibrarysystem.repository.ReadBooksRepository;
import pl.edu.agh.managementlibrarysystem.repository.UserRepository;
import pl.edu.agh.managementlibrarysystem.utils.Alerts;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final IssuedBooksRepository issuedBooksRepository;
    private final ReadBooksRepository readBooksRepository;
    private final Mapper<User, UserDTO> userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Getter
    private User loggedUser;

    @Transactional
    public Optional<User> addNormalUser(String name, String surname, String email, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        User u = new User(name, surname, email, encodedPassword, Permission.NORMAL_USER);
        return Optional.of(repository.save(u));
    }
    @Transactional
    public UserDTO findById(long l) {
        return repository.findById(l).map(this.userMapper::mapToDto).orElse(null);
    }

    @Transactional
    public Optional<User> login(String email, String password){
        Optional<User> userOptional = repository.findByEmail(email);
        if(userOptional.isEmpty()){
            Alerts.showErrorAlert("User not found.","Email "+email+" not found");
            return userOptional;
        }
        if(!passwordEncoder.matches(password,userOptional.get().getPassword())){
            Alerts.showErrorAlert("Password incorrect.","Password incorrect");
            return Optional.empty();
        }
        return userOptional;

    }

}
