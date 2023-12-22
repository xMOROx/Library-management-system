package pl.edu.agh.managementlibrarysystem.controller.entry;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.BaseDataEntryController;
import pl.edu.agh.managementlibrarysystem.event.fxml.NewItemAddedEvent;
import pl.edu.agh.managementlibrarysystem.model.Genre;
import pl.edu.agh.managementlibrarysystem.service.GenresService;
import pl.edu.agh.managementlibrarysystem.utils.Alerts;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

@Controller
@RequiredArgsConstructor
public class GenresDataEntryController extends BaseDataEntryController<ActionEvent> implements Initializable {
    private final GenresService genresService;
    @FXML
    private ComboBox<String> parentGenreComboBox;
    @FXML
    private MFXTextField genreTypeInput;
    private ObservableList<String> mainGenres;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.mainGenres = this.genresService.getMainGenres();
        this.parentGenreComboBox.setValue("Leave empty or choose type");
        this.parentGenreComboBox.setItems(this.mainGenres);
        this.save.disableProperty().bind(this.genreTypeInput.textProperty().isEmpty());
    }

    @FXML
    @Override
    protected void save(ActionEvent actionEvent) {
        String numberRegex = ".*\\d.*";
        String genreTypeInputText = this.genreTypeInput.getText();
        Genre parentGenre = null;

        if (!Objects.equals(this.parentGenreComboBox.getValue(), "Leave empty or choose type")) {
            parentGenre = this.genresService.getGenreByType(this.parentGenreComboBox.getValue());
        }


        if (genreTypeInputText.matches(numberRegex)) {
            Alerts.showErrorAlert("Wrong input", "Genre type cannot contain a number");
            return;
        }

        if (this.genresService.saveGenre(
                Genre.builder()
                        .genre(genreTypeInputText)
                        .parentGenre(parentGenre)
                        .build()
        )) {
            this.root.fireEvent(new NewItemAddedEvent(NewItemAddedEvent.NEW_GENRE));
            Alerts.showAddingAlert("Genre added", "Genre has been added successfully", genreTypeInputText);
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
        this.genreTypeInput.clear();
        this.parentGenreComboBox.setValue("Leave empty or choose type");
    }
}
