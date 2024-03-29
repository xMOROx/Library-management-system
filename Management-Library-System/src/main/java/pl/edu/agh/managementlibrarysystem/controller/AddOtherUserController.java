package pl.edu.agh.managementlibrarysystem.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.BaseController;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.ResizeableBaseController;
import pl.edu.agh.managementlibrarysystem.model.util.Permission;
import pl.edu.agh.managementlibrarysystem.service.UserService;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class AddOtherUserController extends ResizeableBaseController implements Initializable {

    private final UserService userService;
    private final Pattern patternEmail;

    @FXML
    public MFXButton addLibrarian;
    @FXML
    public MFXButton  addAdmin;
    @FXML
    public Label fullscreen;
    @FXML
    public Label unfullscreen;

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

    private BooleanProperty nameBool;
    private BooleanProperty surnameBool;
    private BooleanProperty emailBool;
    private BooleanProperty passwordBool;
    private BooleanProperty repeatPasswordBool;

    public AddOtherUserController(ApplicationContext applicationContext, UserService userService) {
        super(applicationContext);
        this.patternEmail = Pattern.compile(".+@.+\\..+", Pattern.CASE_INSENSITIVE);
        this.userService = userService;
    }

    private static void errorLabel(String error_message, Color c, ObservableList<Node> list) {
        Label l = new Label();
        l.setText(error_message);
        l.setTextFill(c);
        list.add(l);
    }

    @FXML
    public void addUserClicked(MouseEvent mouseEvent) {
        this.userService.addUser(name.getText(), surname.getText(), email.getText(), password.getText(),Permission.NORMAL_USER);
        clearFields();

    }
    public void addAdminClicked(MouseEvent mouseEvent){
        this.userService.addUser(name.getText(), surname.getText(), email.getText(), password.getText(),Permission.ADMIN);
        clearFields();

    }
    public void addLibrarianClicked(MouseEvent mouseEvent){
        this.userService.addUser(name.getText(), surname.getText(), email.getText(), password.getText(), Permission.LIBRARIAN);
        clearFields();
    }

    private void clearFields() {
        name.clear();
        surname.clear();
        email.clear();
        password.clear();
        repeatPassword.clear();
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
        this.tooltipInitializer();

        this.nameBool = new SimpleBooleanProperty(false);
        this.surnameBool = new SimpleBooleanProperty(false);
        this.emailBool = new SimpleBooleanProperty(false);
        this.passwordBool = new SimpleBooleanProperty(false);
        this.repeatPasswordBool = new SimpleBooleanProperty(false);

        this.addUser.disableProperty().bind(Bindings.createBooleanBinding(() -> !(nameBool.get() && surnameBool.get() && emailBool.get() && passwordBool.get() && repeatPasswordBool.get())
                , nameBool, surnameBool, emailBool, passwordBool, repeatPasswordBool));
        this.addLibrarian.disableProperty().bind(Bindings.createBooleanBinding(() -> !(nameBool.get() && surnameBool.get() && emailBool.get() && passwordBool.get() && repeatPasswordBool.get())
                , nameBool, surnameBool, emailBool, passwordBool, repeatPasswordBool));
        this.addAdmin.disableProperty().bind(Bindings.createBooleanBinding(() -> !(nameBool.get() && surnameBool.get() && emailBool.get() && passwordBool.get() && repeatPasswordBool.get())
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

}
