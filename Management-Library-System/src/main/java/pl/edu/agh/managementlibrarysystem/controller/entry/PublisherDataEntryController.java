package pl.edu.agh.managementlibrarysystem.controller.entry;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.BaseDataEntryController;
import pl.edu.agh.managementlibrarysystem.service.PublisherService;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class PublisherDataEntryController extends BaseDataEntryController<ActionEvent> implements Initializable {
    private final PublisherService publisherService;
    @FXML
    private Label publisherName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.save.disableProperty().bind(this.publisherName.textProperty().isEmpty());
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
