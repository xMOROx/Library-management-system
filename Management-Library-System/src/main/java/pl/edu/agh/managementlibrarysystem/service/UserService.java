package pl.edu.agh.managementlibrarysystem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.agh.managementlibrarysystem.DTO.UserDTO;
import pl.edu.agh.managementlibrarysystem.mapper.abstraction.Mapper;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.model.util.Permission;
import pl.edu.agh.managementlibrarysystem.repository.IssuedBooksRepository;
import pl.edu.agh.managementlibrarysystem.repository.ReviewBookRepository;
import pl.edu.agh.managementlibrarysystem.repository.UserRepository;
import pl.edu.agh.managementlibrarysystem.session.UserSession;
import pl.edu.agh.managementlibrarysystem.utils.Alerts;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final IssuedBooksRepository issuedBooksRepository;
    private final ReviewBookRepository readBooksRepository;
    private final Mapper<User, UserDTO> userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserSession session;


    @Transactional
    public Optional<User> addUser(String name, String surname, String email, String password, Permission permission) {
        Optional<User> potentialDuplicate = repository.findByEmail(email);
        if (potentialDuplicate.isPresent()) {
            Alerts.showErrorAlert("Incorrect Email.", "User with email " + email + " is already signed up");
            return Optional.empty();
        }
        String encodedPassword = passwordEncoder.encode(password);
        User u = new User(name, surname, email, encodedPassword, permission);
        Alerts.showInformationAlert("User added.", "User added succsesfully");
        return Optional.of(repository.save(u));
    }

    public UserDTO findById(long l) {
        return repository.findById(l).map(this.userMapper::mapToDto).orElse(null);
    }

    @Transactional
    public Optional<User> login(String email, String password) {
        Optional<User> userOptional = repository.findByEmail(email);
        if (userOptional.isEmpty()) {
            Alerts.showErrorAlert("User not found.", "Email " + email + " not found");
            return userOptional;
        }
        if (!passwordEncoder.matches(password, userOptional.get().getPassword())) {
            Alerts.showErrorAlert("Password incorrect.", "Password incorrect");
            return Optional.empty();
        }
        session.setLoggedUser(userOptional.get());
        return userOptional;

    }

    @Transactional
    public List<UserDTO> getAllUsers() {
        return repository.findAll().stream()
                .map(this.userMapper::mapToDto)
                .toList();
    }

    @Transactional
    public void deleteByUserId(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public void updateUser(Long id, String firstname, String lastname, String email, String password, Permission permission) {
        repository.updateUser(id, firstname, lastname, email, password, permission);
    }

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }


    public void changePassword(User user) {
        repository.save(user);
    }
}
