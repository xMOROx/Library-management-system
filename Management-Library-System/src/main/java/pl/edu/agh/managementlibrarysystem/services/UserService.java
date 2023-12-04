package pl.edu.agh.managementlibrarysystem.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.model.util.Permission;
import pl.edu.agh.managementlibrarysystem.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserService {
    private UserRepository repository;
    public UserService(UserRepository repository){
        this.repository = repository;
    }
    @Transactional
    public Optional<User> addUser(String name, String surname, String email, String password) {

        User u = new User(name,surname,email,password, Permission.NORMAL_USER);
        repository.save(u);
        return Optional.of(u);
    }
}
