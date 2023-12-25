package pl.edu.agh.managementlibrarysystem.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.BaseController;
import pl.edu.agh.managementlibrarysystem.event.MainAppReadyEvent;
import pl.edu.agh.managementlibrarysystem.event.OpenWindowEvent;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class LoginController extends BaseController implements Initializable {

    private final Resource createUserFxml;
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
    private MFXButton login;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private AnchorPane parentRoot;

    public LoginController(@Value("classpath:/fxml/create-user.fxml") Resource createUserFxml,
                           ApplicationContext applicationContext) {
        super(applicationContext);
        this.createUserFxml = createUserFxml;
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.tooltipInitializer();
        playTransitionAnimation();
        requestFocus(usernameTextField);
        requestFocus(passwordTextField);
    }

    private void playTransitionAnimation() {
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
    }

    @FXML
    private void login(ActionEvent actionEvent) {
        applicationContext.publishEvent(new MainAppReadyEvent((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()));
    }

    @FXML
    private void signIn(KeyEvent keyEvent) {

    }

    @FXML
    private void loadPasswordRetrievalPanel(MouseEvent mouseEvent) {

    }

    @FXML
    private void createAccount(MouseEvent mouseEvent) {
        applicationContext.publishEvent(new OpenWindowEvent((Stage) ((Node) mouseEvent.getSource()).getScene().getWindow(), this.createUserFxml));
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
