package pl.edu.agh.managementlibrarysystem.controller.entry;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.BaseDataEntryController;
import pl.edu.agh.managementlibrarysystem.event.fxml.NewItemAddedEvent;
import pl.edu.agh.managementlibrarysystem.model.Publisher;
import pl.edu.agh.managementlibrarysystem.service.PublisherService;
import pl.edu.agh.managementlibrarysystem.utils.Alerts;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class PublisherDataEntryController extends BaseDataEntryController<ActionEvent> implements Initializable {
    private final PublisherService publisherService;
    @FXML
    private MFXTextField publisherNameInput;
    @FXML
    private Label publisherName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.save.disableProperty().bind(this.publisherName.textProperty().isEmpty());
    }

    @FXML
    @Override
    protected void save(ActionEvent actionEvent) {
        String numberRegex = ".*\\d.*";
        String publisherNameInputText = this.publisherNameInput.getText();


        if (publisherNameInputText.matches(numberRegex)) {
            Alerts.showErrorAlert("Wrong input", "Publisher name cannot contain a number");
            return;
        }

        if (this.publisherService.savePublisher(
                Publisher.builder()
                        .name(publisherNameInputText)
                        .build()
        )) {
            this.root.fireEvent(new NewItemAddedEvent(NewItemAddedEvent.NEW_PUBLISHER));
            Alerts.showAddingAlert("Publisher added", "Publisher has been added successfully", publisherNameInputText);
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
        this.publisherNameInput.clear();
    }
}
