package pl.edu.agh.managementlibrarysystem.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.config.events.MainAppReadyEvent;
import pl.edu.agh.managementlibrarysystem.config.events.StageReadyEvent;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
@RequiredArgsConstructor
public class LoginController implements Initializable {

    private final ApplicationContext applicationContext;

    @FXML
    private Label welcome;
    @FXML
    private Label userLogin;
    @FXML
    private Label username;
    @FXML
    private TextField usernameTextField;
    @FXML
    private Label password;
    @FXML
    private Label close;
    @FXML
    private Label minimize;
    @FXML
    private MFXButton login;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private AnchorPane parentRoot;

    static void tooltipFactory(Label close, Label minimize) {
        Tooltip closeApp = new Tooltip("Close");
        closeApp.setStyle("-fx-font-size:11");
        closeApp.setMinSize(20, 20);
        close.setTooltip(closeApp);
        Tooltip minimizeApp = new Tooltip("Minimize");
        minimizeApp.setStyle("-fx-font-size:11");
        minimizeApp.setMinSize(20, 20);
        minimize.setTooltip(minimizeApp);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tooltipFactory(close, minimize);
        TranslateTransition translateWelcomeText = new TranslateTransition(Duration.millis(1500), welcome);
        translateWelcomeText.setToX(50);
        TranslateTransition translateTransitionUserLogin = new TranslateTransition(Duration.millis(1500), userLogin);
        translateTransitionUserLogin.setToX(-93);
        TranslateTransition translateUsernameLabel = new TranslateTransition(Duration.millis(1500), username);
        translateUsernameLabel.setToX(30);
        TranslateTransition translateUsernameField = new TranslateTransition(Duration.millis(1500), usernameTextField);
        translateUsernameField.setToX(27);
        TranslateTransition translatePasswordLabel = new TranslateTransition(Duration.millis(1500), password);
        translatePasswordLabel.setToX(30);
        TranslateTransition translatePasswordField = new TranslateTransition(Duration.millis(1500), passwordTextField);
        translatePasswordField.setToX(27);
        TranslateTransition translateButton = new TranslateTransition(Duration.millis(1500), login);
        translateButton.setToX(-48);
        FadeTransition fadeParentRoot = new FadeTransition(Duration.seconds(2), parentRoot);
        fadeParentRoot.setFromValue(0);
        fadeParentRoot.setToValue(1);
        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(fadeParentRoot, translateWelcomeText, translateTransitionUserLogin, translateUsernameLabel, translateUsernameField, translatePasswordLabel, translatePasswordField, translateButton);
        parallelTransition.play();
        requestFocus(usernameTextField);
        requestFocus(passwordTextField);
    }

    @FXML
    private void close(MouseEvent mouseEvent) {
        ((ConfigurableApplicationContext) applicationContext).close();
        Platform.exit();
    }

    @FXML
    private void minimize(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void login(ActionEvent actionEvent) {
        applicationContext.publishEvent(new MainAppReadyEvent((Stage)((Node)actionEvent.getSource()).getScene().getWindow()));
    }

    @FXML
    private void signIn(KeyEvent keyEvent) {

    }

    @FXML
    private void loadPasswordRetrievalPanel(MouseEvent mouseEvent) {

    }

    @FXML
    private void createAccount(MouseEvent mouseEvent) {

    }

    private void requestFocus(TextField field) {
        field.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.DOWN) {
                passwordTextField.requestFocus();
            }
            if (event.getCode() == KeyCode.UP) {
                usernameTextField.requestFocus();
            }
        });
    }
}
