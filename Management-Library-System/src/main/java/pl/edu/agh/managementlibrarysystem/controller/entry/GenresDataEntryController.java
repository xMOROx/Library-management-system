package pl.edu.agh.managementlibrarysystem.controller.entry;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.BaseDataEntryController;
import pl.edu.agh.managementlibrarysystem.service.GenresService;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
@RequiredArgsConstructor
public class GenresDataEntryController extends BaseDataEntryController<ActionEvent> implements Initializable {
    private final GenresService genresService;
    @FXML
    private ComboBox<String> parentGenreComboBox;
    @FXML
    private MFXTextField genreTypeInput;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.parentGenreComboBox.setValue("Leave empty or choose type");
        this.save.disableProperty().bind(this.genreTypeInput.textProperty().isEmpty());
    }

    @FXML
    @Override
    protected void save(ActionEvent actionEvent) {

    }

    @FXML
    @Override
    protected void cancel(ActionEvent event) {
        this.root.fireEvent(new BookDataEntryController.EntryHelperEmptyEvent());
        this.root.setCenter(null);
    }
}
