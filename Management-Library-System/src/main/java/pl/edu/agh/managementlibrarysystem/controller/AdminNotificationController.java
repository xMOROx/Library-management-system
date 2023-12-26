package pl.edu.agh.managementlibrarysystem.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.BaseDataEntryController;
import pl.edu.agh.managementlibrarysystem.event.fxml.LeavingBorderPaneEvent;
import pl.edu.agh.managementlibrarysystem.model.Notification;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
@RequiredArgsConstructor
public class AdminNotificationController extends BaseDataEntryController<ActionEvent> implements Initializable {

    public MFXTextField bookSearchField;
    public Text bookTitle;
    public Text bookAuthor;
    public Text bookPublisher;
    public Text availability;
    public MFXTextField userSearchTextField;
    public MFXTextField numberOfDaysTextField;
    public Text userFullName;
    public Text userEmail;
    public MFXTextField dateTextField;
    public MFXButton makeNotification;
    public Label close;
    public MFXButton backBtn;

    @FXML
    public void back(ActionEvent actionEvent){
        this.root.fireEvent(new LeavingBorderPaneEvent(LeavingBorderPaneEvent.LEAVING));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        this.save.disableProperty().bind(this.authorNameInput.textProperty().isEmpty().or(this.authorLastnameInput.textProperty().isEmpty()));
    }
    @FXML
    @Override
    protected void save(ActionEvent event) {

    }
    @FXML
    @Override
    protected void cancel(ActionEvent event) {

    }

    @Override
    protected void clearFields() {

    }

    public void close(MouseEvent mouseEvent) {
    }

    public void searchBook(KeyEvent keyEvent) {
    }

    public void searchUser(KeyEvent keyEvent) {
    }

    public void enterNumberOfDays(KeyEvent keyEvent) {
    }

    public void enterDate(KeyEvent keyEvent) {
    }

    public void makeNotification(ActionEvent actionEvent) {
    }
}
