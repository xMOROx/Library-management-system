package pl.edu.agh.managementlibrarysystem.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.managementlibrarysystem.model.Author;
import pl.edu.agh.managementlibrarysystem.repository.AuthorRepository;
import pl.edu.agh.managementlibrarysystem.utils.Alerts;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;

    public boolean saveAuthor(Author author) {
        String firstName = author.getFirstName();
        String lastName = author.getLastName();

        if (this.authorRepository.findByNameAndLastname(firstName, lastName).isEmpty()) {
            authorRepository.save(author);
            return true;
        }

        Alerts.showErrorAlert("Author already exist", "Author with given name " + firstName + " and lastname " + lastName + " already exists");
        return false;
    }

    public ObservableList<String> getAllAuthors() {
        return FXCollections.observableList(
                this.authorRepository.findAll()
                        .stream()
                        .map(author -> author.getFirstName() + " " + author.getLastName())
                        .toList()
        );

    }

    public Author getAuthorByNameAndLastname(String name, String lastname) {
        return this.authorRepository.findByNameAndLastname(
                name,
                lastname
        ).orElse(null);
    }
}
