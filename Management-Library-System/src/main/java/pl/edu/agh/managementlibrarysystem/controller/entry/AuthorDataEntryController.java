package pl.edu.agh.managementlibrarysystem.controller.entry;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.BaseDataEntryController;
import pl.edu.agh.managementlibrarysystem.model.Author;
import pl.edu.agh.managementlibrarysystem.service.AuthorService;

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
        authorService.saveAuthor(
                Author.builder()
                        .firstName(this.authorNameInput.getText())
                        .lastName(this.authorLastnameInput.getText())
                        .build()
        );
    }

    @FXML
    @Override
    protected void cancel(ActionEvent event) {
        this.root.fireEvent(new BookDataEntryController.EntryHelperEmptyEvent());
        this.root.setCenter(null);
    }
}
