package pl.edu.agh.managementlibrarysystem.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.BaseController;
import pl.edu.agh.managementlibrarysystem.event.BorderPaneReadyEvent;
import pl.edu.agh.managementlibrarysystem.event.OpenWindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

@Controller
@RequiredArgsConstructor
public class MainStageController extends BaseController implements Initializable {

    public BorderPane pane;

    @FXML
    private BorderPane borderpane;
    @FXML
    private VBox homeButtonsPanel;
    @FXML
    private MFXButton back;
    @FXML
    private MFXButton home;
    @FXML
    private MFXButton books;
    @FXML
    private MFXButton students;
    @FXML
    private MFXButton issuedBooks;
    @FXML
    private MFXButton returnBooks;
    @FXML
    private MFXButton allIssuedBooks;
    @FXML
    private MFXButton announcement;
    @FXML
    private MFXButton closeButton;
    @FXML
    private Label testLabel;

    @Autowired
    public MainStageController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pane = borderpane;
        back.setTooltip(new Tooltip("Logout"));
    }

    @FXML
    private void stageDragged(MouseEvent mouseEvent) {

    }

    @FXML
    private void stagePressed(MouseEvent mouseEvent) {
    }

    @FXML
    private void logout(ActionEvent actionEvent) {
        applicationContext
                .publishEvent(new OpenWindowEvent((Stage) ((Node) actionEvent.getSource()).getScene().getWindow(),
                        new ClassPathResource("fxml/login.fxml")));
    }

    @FXML
    private void loadHomePanel(ActionEvent actionEvent) {

    }

    @FXML
    private void loadBooksPanel(ActionEvent actionEvent) throws IOException {
        applicationContext.publishEvent(new BorderPaneReadyEvent(pane, new ClassPathResource("fxml/books.fxml")));

    }

    @FXML
    private void loadStudentPanel(ActionEvent actionEvent) {

    }

    @FXML
    private void loadIssueBooksPanel(ActionEvent actionEvent) {

    }

    @FXML
    private void loadReturnBooksPanel(ActionEvent actionEvent) {

    }

    @FXML
    private void viewAllIssuedBooks(ActionEvent actionEvent) {
        applicationContext.publishEvent(new BorderPaneReadyEvent(pane, new ClassPathResource("fxml/issuedBooks.fxml")));
    }

    @FXML
    private void loadSendAnnouncementsPanel(ActionEvent actionEvent) {

    }

    @FXML
    private void loadSettingsPanel(ActionEvent actionEvent) {

    }

    @FXML
    private void closeApp(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to close the application ?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.isPresent() && option.get() == ButtonType.OK) {
            ((ConfigurableApplicationContext) applicationContext).close();
            Platform.exit();
        }
    }


}
