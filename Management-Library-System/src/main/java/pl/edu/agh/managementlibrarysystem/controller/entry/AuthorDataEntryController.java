package pl.edu.agh.managementlibrarysystem.controller.entry;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.BaseDataEntryController;
import pl.edu.agh.managementlibrarysystem.event.fxml.NewItemAddedEvent;
import pl.edu.agh.managementlibrarysystem.model.Author;
import pl.edu.agh.managementlibrarysystem.service.AuthorService;
import pl.edu.agh.managementlibrarysystem.utils.Alerts;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
@RequiredArgsConstructor
public class AuthorDataEntryController extends BaseDataEntryController<ActionEvent> implements Initializable {

    private final AuthorService authorService;

    @FXML
    private MFXTextField authorNameInput;
    @FXML
    private MFXTextField authorLastnameInput;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.save.disableProperty().bind(this.authorNameInput.textProperty().isEmpty().or(this.authorLastnameInput.textProperty().isEmpty()));
    }

    @FXML
    @Override
    protected void save(ActionEvent actionEvent) {
        String numberRegex = ".*\\d.*";
        String authorName = this.authorNameInput.getText();
        String authorLastname = this.authorLastnameInput.getText();


        if (authorName.matches(numberRegex)) {
            Alerts.showErrorAlert("Wrong input", "Author name cannot contain a number");
            return;
        }

        if (authorLastname.matches(numberRegex)) {
            Alerts.showErrorAlert("Wrong input", "Author lastname cannot contain a number");
            return;
        }


        if (authorService.saveAuthor(
                Author.builder()
                        .firstname(Character.toUpperCase(authorName.charAt(0)) + authorName.substring(1).toLowerCase())
                        .lastname(Character.toUpperCase(authorLastname.charAt(0)) + authorLastname.substring(1).toLowerCase())
                        .build()
        )) {
            this.root.fireEvent(new NewItemAddedEvent(NewItemAddedEvent.NEW_AUTHOR));
            Alerts.showAddingAlert("Author added", "Author has been added successfully", authorName + " " + authorLastname);
            this.cancel(actionEvent);

        }

        this.clearFields();
    }

    @FXML
    @Override
    protected void cancel(ActionEvent event) {
        this.root.fireEvent(new BookDataEntryController.EntryHelperEmptyEvent());
        this.root.setCenter(null);
    }

    @Override
    protected void clearFields() {
        this.authorNameInput.clear();
        this.authorLastnameInput.clear();
    }
}
