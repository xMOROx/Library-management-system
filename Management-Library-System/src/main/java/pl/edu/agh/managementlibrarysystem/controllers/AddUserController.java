package pl.edu.agh.managementlibrarysystem.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.config.events.CommonActions;
import pl.edu.agh.managementlibrarysystem.config.events.OpenWindowEvent;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Controller

public class AddUserController implements Initializable {

    private final ApplicationContext applicationContext;

    private final Resource backWindow;
    private final Pattern patternEmail;
    @FXML
    private TextField name;
    @FXML
    private TextField surname;
    @FXML
    private TextField email;
    @FXML
    private MFXButton addUser;
    @FXML
    private Label error;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField repeatPassword;
    @FXML
    private MFXButton cancel;
    @FXML
    private VBox errorVbox;
    @FXML
    private Label close;
    @FXML
    private Label minimize;
    private BooleanProperty nameBool;
    private BooleanProperty surnameBool;
    private BooleanProperty emailBool;
    private BooleanProperty passwordBool;
    private BooleanProperty repeatPasswordBool;

    public AddUserController(@Value("classpath:/fxml/login.fxml") Resource backWindow,
                             ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.backWindow = backWindow;
        this.patternEmail = Pattern.compile(".+@.+\\..+", Pattern.CASE_INSENSITIVE);
    }

    private static void errorLabel(String error_message, Color c, ObservableList<Node> list) {
        Label l = new Label();
        l.setText(error_message);
        l.setTextFill(c);
        list.add(l);
    }

    @FXML
    public void addClicked(MouseEvent mouseEvent) {
        System.out.println("Adding works");
    }

    @FXML
    public void cancelClicked(MouseEvent mouseEvent) {
        applicationContext.publishEvent(new OpenWindowEvent((Stage) ((Node) mouseEvent.getSource()).getScene().getWindow(), backWindow));
    }

    @FXML
    public void keyTyped() {
        updateErrorList();
    }

    public void updateErrorList() {
        ObservableList<Node> list = this.errorVbox.getChildren();
        list.clear();
        Color c = Color.color(1, 0, 0);
        if (!nameBool.get()) {
            errorLabel("Problem with name", c, list);
        }
        if (!surnameBool.get()) {
            errorLabel("Problem with surname", c, list);
        }
        if (!emailBool.get()) {
            errorLabel("Problem with email", c, list);
        }
        if (!passwordBool.get()) {
            errorLabel("Problem with password", c, list);
        }
        if (!repeatPasswordBool.get()) {
            errorLabel("Passwords aren't equal", c, list);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CommonActions.tooltipInitializer(close, minimize);

        this.nameBool = new SimpleBooleanProperty(false);
        this.surnameBool = new SimpleBooleanProperty(false);
        this.emailBool = new SimpleBooleanProperty(false);
        this.passwordBool = new SimpleBooleanProperty(false);
        this.repeatPasswordBool = new SimpleBooleanProperty(false);

        this.addUser.disableProperty().bind(Bindings.createBooleanBinding(() -> !(nameBool.get() && surnameBool.get() && emailBool.get() && passwordBool.get() && repeatPasswordBool.get())
                , nameBool, surnameBool, emailBool, passwordBool, repeatPasswordBool));

        validateName(nameBool, name);

        validateName(surnameBool, surname);

        validateEmail();

        validatePassword();
    }

    private void validateEmail() {
        emailBool.bind(Bindings.createBooleanBinding(() -> {
            String text = email.textProperty().get();
            Matcher matcher = this.patternEmail.matcher(text);
            return matcher.find();
        }, email.textProperty()));
    }

    private void validateName(BooleanProperty nameBool, TextField name) {
        nameBool.bind(Bindings.createBooleanBinding(() -> {
            String text = name.textProperty().get();
            if (text.length() < 2) {
                return false;
            } else return text.toUpperCase().charAt(0) == text.charAt(0);

        }, name.textProperty()));
    }

    private void validatePassword() {
        passwordBool.bind(Bindings.createBooleanBinding(() -> password.getText().length() >= 8, password.textProperty()));

        repeatPasswordBool.bind(Bindings.createBooleanBinding(() -> password.getText().equals(repeatPassword.getText()), repeatPassword.textProperty(), password.textProperty()));
        name.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {

            }
        });
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
}
