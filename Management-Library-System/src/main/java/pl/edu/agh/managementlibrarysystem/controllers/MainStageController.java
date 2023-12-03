package pl.edu.agh.managementlibrarysystem.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.config.events.BorderPaneReadyEvent;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class MainStageController {
    private final ApplicationContext applicationContext;

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
    private MFXButton close;
    @FXML
    private Label testLabel;

    @FXML
    private void stageDragged(MouseEvent mouseEvent) {

    }

    @FXML
    private void stagePressed(MouseEvent mouseEvent) {
    }

    @FXML
    private void logout(ActionEvent actionEvent) {

    }

    @FXML
    private void loadHomePanel(ActionEvent actionEvent) {

    }

    @FXML
    private void loadBooksPanel(ActionEvent actionEvent) throws IOException {

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

    }

    @FXML
    private void loadSendAnnouncementsPanel(ActionEvent actionEvent) {

    }

    @FXML
    private void loadSettingsPanel(ActionEvent actionEvent) {

    }

    @FXML
    private void closeApp(ActionEvent actionEvent) {

    }
}
